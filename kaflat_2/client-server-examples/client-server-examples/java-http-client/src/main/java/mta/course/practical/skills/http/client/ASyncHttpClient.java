package mta.course.practical.skills.http.client;

import okhttp3.*;

import java.io.IOException;

public class ASyncHttpClient {

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        String BASE_URL = "http://localhost:8080/test";
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        Call call = client.newCall(request);

        System.out.println("About to invoke a call synchronously to: [" + BASE_URL + "]...");

        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Got a response back ! HIP HIP HEREY !");
                Headers headers = response.headers();
                System.out.println("Code: " + response.code());
                System.out.println("Total Headers: " + headers.size());
                System.out.println("Headers Names: " + headers.names());
                headers.names().forEach(headerName -> {
                    System.out.println("Header [" + headerName + "] = [" + headers.get(headerName) + "]");
                });
                System.out.println("Body: " + response.body().string());
            }

            public void onFailure(Call call, IOException e) {
                System.out.println("Oopsy... something went wrong..." + e.getMessage());
            }
        });

        System.out.println("I will be printed immediately");
    }

}
