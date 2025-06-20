package org.ezcode.codetest.application.submission.port;

public interface ExceptionNotifier {

    void sendEmbed(String title, String description, String exception, String methodName);

}
