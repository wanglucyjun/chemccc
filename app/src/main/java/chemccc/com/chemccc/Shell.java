package chemccc.com.chemccc;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeoutException;

public class Shell {
    public interface OnExecuteListener {

        void onExecuteSucceed();

        void onExecutedFailed();
    }

    public interface OnOutputListener {

        void onOutputResult(String content);

        void onErrorOutputResult(String content);
    }

    private static final int DEFAULT_TIMEOUT_INTERVAL = 500;
    private static final long DEFAULT_TIMEOUT = 6 * 60 * 1000;

    private long interval;
    private long timeout = -1;

    private Runtime runtime = Runtime.getRuntime();

    public Shell(long timeout, long interval) {
        this.timeout = timeout;
        this.interval = interval;
    }

    public Shell(long timeout) {
        this(timeout, DEFAULT_TIMEOUT_INTERVAL);
    }

    public Shell() {
        this(DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_INTERVAL);
    }

    public void execute(String command, OnExecuteListener executeListener) {
        try {
         //   execute(command, false, null, null);
            execute(command, false, executeListener, null);
            if (executeListener != null) {
                executeListener.onExecuteSucceed();
            }
        } catch (Exception e) {
            //AppLog.e("execute cmd error", e);
            if (executeListener != null) {
                executeListener.onExecutedFailed();
            }
        }
    }

    public void execute(String command, boolean isInRoot, OnExecuteListener executeListener, OnOutputListener ol) {
        try {
            executes(command, isInRoot, executeListener, ol);
            if (executeListener != null) {
                executeListener.onExecuteSucceed();
            }
        } catch (Exception e) {
           // AppLog.e("execute cmd error", e);
            if (executeListener != null) {
                executeListener.onExecutedFailed();
            }
        }
    }

    private void executes(String command, boolean isInRoot, OnExecuteListener executeListener, OnOutputListener ol)
            throws Exception {
        if (command == null) {
            throw new NullPointerException();
        }
        //
        Process process;
        if (isInRoot) {
            process = runtime.exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.flush();
            os.close();
        } else {
            process = runtime.exec(command);
        }
        long limitTime = timeout + System.currentTimeMillis();
        Integer status = null;
        do {
            try {
                // not blocked
                status = process.exitValue();
                break;
            } catch (IllegalThreadStateException e) {
                try {
                    Thread.sleep(getInterval());
                } catch (InterruptedException we) {
                }
            }
        } while (System.currentTimeMillis() < limitTime);
        //
        if (status == null) {
            process.destroy();
            try {
                status = process.waitFor();
            } catch (InterruptedException e) {
                // or do something
                throw new TimeoutException();
            }
        }

        handleOutput(process, ol, executeListener != null ? executeListener : null, isInRoot);
    }

    public void executeUIA(String command, boolean isInRoot, OnExecuteListener executeListener, OnOutputListener ol) {
        try {
            executes(command, isInRoot, executeListener, ol);
        } catch (Exception e) {
            //AppLog.e("execute cmd error", e);
        }
    }

    private void handleOutput(Process target, OnOutputListener ol, OnExecuteListener executeListener, boolean isInRoot)
            throws Exception {
        StringBuilder normal = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedInputStream stdOutBis = null;
        BufferedInputStream stdErrBis = null;

        try {
            stdOutBis = new BufferedInputStream(target.getInputStream());
            stdErrBis = new BufferedInputStream(target.getErrorStream());

            int c1;
            while ((c1 = stdOutBis.read()) != -1) {
                normal.append(String.valueOf((char) c1));
            }

            int c2;
            while ((c2 = stdErrBis.read()) != -1) {
                error.append(String.valueOf((char) c2));
            }

            //AppLog.d(normal.toString());
            if (normal.toString().contains("FAILURES")) {
                error.append(normal.toString());
                if(executeListener != null) {
                    executeListener.onExecutedFailed();
                }
            }

            //AppLog.d(error.toString());

            if (ol != null) {
                ol.onOutputResult(normal.toString());
                ol.onErrorOutputResult(error.toString());
            }

            if (executeListener == null && !isInRoot) {
                if (error.toString().length() != 0) {
                    if (error.toString().contains("WARNING")) {
                    } else {
                        throw new Exception();
                    }
                }

                if (normal.toString().contains("FAILURES") || normal.toString().contains("Error")) {
                    throw new Exception();
                }
            }
        } finally {
            try {
                if (stdOutBis != null) {
                    stdOutBis.close();
                }
                if (stdErrBis != null) {
                    stdErrBis.close();
                }
                target.destroy();
            } catch (Exception e) {
               // AppLog.d("close IO", e);
            }
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
