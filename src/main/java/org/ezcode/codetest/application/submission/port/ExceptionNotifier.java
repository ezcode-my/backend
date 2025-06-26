package org.ezcode.codetest.application.submission.port;

public interface ExceptionNotifier {

    void notifyException(String methodName, Throwable t);

}
