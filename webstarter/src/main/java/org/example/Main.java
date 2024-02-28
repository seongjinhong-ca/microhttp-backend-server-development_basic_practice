// my practice for basic backend
package org.example;

// Http server

// Takes HTTP requests

// POST /search HTTP/1.1
// Content-Type: text/html
// User-Agent: Chrome v1.2.3
//
// <DATA>

// Produce HTTP responses

// 200 OK
// Content-Type: text/html
//
// <DATA>

import dev.mccue.microhttp.handler.IntoResponse;
import dev.mccue.reasonphrase.ReasonPhrase;
import org.microhttp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class TextResponse implements IntoResponse {
    private final int status;
    private final List<Header> headers;
    private final String body;

    public TextResponse(int status, List<Header> headers, String body) {
        this.status = status;
        var copied = new ArrayList<>(headers);
        copied.add(new Header("Content-Type", "text/plain; charset=utf-8"));
        this.headers = copied;
        this.body = body;
    }

    public TextResponse(String body) {
        this(200, List.of(), body);
    }

    @Override
    public Response intoResponse() {
        return new Response(
                this.status,
                ReasonPhrase.forStatus(this.status),
                headers,
                body.getBytes()
        );
    }
}
class NewHandler implements dev.mccue.microhttp.handler.Handler {

    @Override
    public IntoResponse handle(Request request) throws Exception {
        if(request.method().equals("GET") && request.uri().equals("/finding")){
            var response = new Response(
                    200,
                    "OK",
                    List.of(
                            new Header("content-Type", "text/html")
                    ),
                    "finding...".getBytes()
            );
            return() ->response;
        } else if(request.method().equals("POST") && request.uri().equals("/search")){
            var response = new Response(
                    200,
                    "OK",
                    List.of(
                            new Header("Content-Type", "text/html"),
                            new Header("User-Agent" , "Chrome v1.2.3")
                    ),
                    "Searching...".getBytes()
            );
            return() ->response;
        } else if(request.method().equals("GET") && request.uri().equals("/hello")){
            var response = new Response(
                    200,
                    "OK",
                    List.of(
                            new Header("content-Type", "text/html")
                    ),
                    "hello...".getBytes()
            );
            return() ->response;
        }else{
            return null;
        }
    }
}
class BasicHandler implements Handler{

    @Override
    public void handle(Request request, Consumer<Response> consumer) {
        Thread.startVirtualThread(()->{
        });
    }
};


public class Main {
    public static void main(String[] args) throws Exception {

        var notFound = new Response(
                404,
                "Not Found",
                List.of(
                        new Header("Content-Type", "text/html")
                ),
                "Not Found Error".getBytes()
        );

        var internalServer = new Response(
                500,
                "Internal Server",
                List.of(
                        new Header("Content-Type", "text/html")
                ),
                "Internal Server Error!".getBytes()
        );

        var handlers = List.of(
                new NewHandler()
        );

        var options = Options.builder().withPort(5523).build();
        EventLoop eventLoop = new EventLoop(options, ((request, consumer) -> {
            try{
                for(var handler: handlers){
                    var intoResponse =handler.handle(request);
                    if(intoResponse != null){
                        consumer.accept(intoResponse.intoResponse());
                        return;
                    }
                }
                consumer.accept(notFound);// 400 not found error
            }catch(Exception e){
                e.printStackTrace();
                consumer.accept(internalServer); // 500 server error
            }
        }));

        eventLoop.start();
        eventLoop.join(); // don't stop

    }
}

