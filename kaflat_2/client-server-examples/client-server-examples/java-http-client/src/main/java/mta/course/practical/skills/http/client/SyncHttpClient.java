package mta.course.practical.skills.http.client;

import okhttp3.*;

import java.io.IOException;

public class SyncHttpClient {

    public static void main(String[] args) {

        OkHttpClient client = new OkHttpClient();

        String BASE_URL = "http://localhost:8080/test";
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        Call call = client.newCall(request);

        try {
            // blocking
            System.out.println("About to invoke a call synchronously to: [" + BASE_URL + "]...");
            final Response response = call.execute();
            System.out.println("Invocation Ended !");

            System.out.println("Code: " + response.code());

            Headers headers = response.headers();
            System.out.println("Total Headers: " + headers.size());
            System.out.println("Headers Names: " + headers.names());
            headers.names().forEach(headerName ->
                    System.out.println("Header [" + headerName + "] = [" + headers.get(headerName) + "]"));

            System.out.println("Body: " + response.body().string());
        } catch (IOException e) {
            System.out.println("Ooops... error occured: " + e.getMessage());
        }
    }
}
