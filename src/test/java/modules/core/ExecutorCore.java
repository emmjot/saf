package modules.core;

import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ExecutorCore {

    private SharedContext ctx;

    // PicoContainer injects class SharedContext
    public ExecutorCore(SharedContext ctx) {
        this.ctx = ctx;
    }
    /**
     * Execute a Command as a background or blocking process.
     *
     * @param cmd     String, Command to execute
     * @param workingDir  File, Working directory
     * @param timeout     Integer, Kill process after this time (in sec) (0: no timeout)
     * @param blocking    Boolean,  Synchronous/blocking (true) or asynchronous/background startup (false).
     * @return  An outputstream that contains the output of the process into stdout/stderr
     */
    public ByteArrayOutputStream execute(String cmd, File workingDir, int timeout, boolean blocking)
    {
        Executor executor = new DefaultExecutor();
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        CommandLine cmdLine = null;

        try {
            cmdLine = CommandLine.parse(cmd);
        } catch (IllegalArgumentException e) {
            Log.error("", e);
        }

        if (timeout > 0)
        {
            ExecuteWatchdog watchdog = new ExecuteWatchdog(1000 * timeout);
            executor.setWatchdog(watchdog);
        }

          /* No live-streaming needed
          PipedOutputStream os = new PipedOutputStream();
          InputStream is = new PipedInputStream(os);
          executor.setStreamHandler(new PumpStreamHandler(os));
          */

        //This is used to end the process when the JVM exits
        ShutdownHookProcessDestroyer processDestroyer = new ShutdownHookProcessDestroyer();
        executor.setProcessDestroyer(processDestroyer);

        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        executor.setStreamHandler(new PumpStreamHandler(os));

        executor.setWorkingDirectory(workingDir);

        Log.debug("Command to execute is " + cmd);
        Log.debug("Working dir is " + workingDir.getAbsolutePath());

        try {
            executor.execute(cmdLine, resultHandler);
        } catch (ExecuteException e) {
            Log.error("", e);
        } catch (IOException e) {
            Log.error("", e);
        }

        if (blocking)
        {
            while ( !resultHandler.hasResult() ) {
                try {
                    resultHandler.waitFor();
                } catch (InterruptedException e) { }
            }
        }

        int exitValue = resultHandler.getExitValue();
        Log.debug("Command execution exitValue is " + exitValue);
        if(executor.isFailure(exitValue)){
            Log.debug("Command execution failed");
        }else{
            Log.debug("Command execution successful");
        }

        return os;
    }


}