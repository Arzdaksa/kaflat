package mta.course.practical.skills.http.client;

import okhttp3.*;

import java.io.IOException;

public class QueryParam {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String BASE_URL = "http://localhost:8080";
        String finalUrl =
                HttpUrl
                    .parse(BASE_URL + "/test/greeting")
                    .newBuilder()
                    .addQueryParameter("name", "Eli")
                    .addQueryParameter("last", "Kopter")
                    .build()
                    .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = client.newCall(request);

        System.out.println("About to invoke URL: [" + finalUrl + "]");

        // synchronous call
        Response response = call.execute();
        System.out.println("Response:");
        System.out.println(response.body().string());
        response.close();

/*
        // asynchronous call
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
                    throws IOException {
                System.out.println("Response:");
                System.out.println(response.body().string());
            }

            public void onFailure(Call call, IOException e) {
                System.out.println("Oopsy... something went wrong..." + e.getMessage());
            }
        });
*/

        System.out.println("Main is done...");
    }
}
