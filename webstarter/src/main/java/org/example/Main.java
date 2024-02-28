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

import org.microhttp.*;

import java.util.List;
import java.util.function.Consumer;

class NewHandler implements Handler{

    @Override
    public void handle(Request request, Consumer<Response> consumer) {

    }
}
class BasicHandler implements Handler{

    @Override
    public void handle(Request request, Consumer<Response> consumer) {
        Thread.startVirtualThread(()->{
            try{
                if(request.method().equals("GET") && request.uri().equals("/finding")){
                    var response = new Response(
                            200,
                            "OK",
                            List.of(
                                    new Header("content-Type", "text/html")
                            ),
                            "finding...".getBytes()
                    );
                    consumer.accept(response);
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
                    consumer.accept(response);
                } else if(request.method().equals("GET") && request.uri().equals("/hello")){

                    /*
                    *
                    * {
                    * Key: value
                    *
                    * {GET, "/hello"}
                    *   :
                    * ()->{
                    * var response = new Response(
                            200,
                            "OK",
                            List.of(
                                    new Header("content-Type", "text/html")
                            ),
                            "hello...".getBytes()
                    );
                    * consumer.accept(response);
                    * } ()
                    *
                    *
                    * }
                    * */
                    var response = new Response(
                            200,
                            "OK",
                            List.of(
                                    new Header("content-Type", "text/html")
                            ),
                            "hello...".getBytes()
                    );
                    consumer.accept(response);
                }else{
                    var response = new Response(
                            404,
                            "Not Found",
                            List.of(
                                    new Header("Content-Type", "text/html")
                            ),
                            "Not Found Error".getBytes()
                    );
                    consumer.accept(response);
                }
            } catch (Exception e){
                var response = new Response(
                        500,
                        "Internal Server",
                        List.of(
                                new Header("Content-Type", "text/html")
                        ),
                        "Internal Server Error!".getBytes()
                );
                consumer.accept(response);
            }
        });
    }
};


public class Main {
    public static void main(String[] args) throws Exception {
//        var handler = new BasicHandler();
//
//        var options = Options.builder()
//                .withPort(5523)
//                .build();
//
//        var eventLoop = new EventLoop(options, handler);
//
//        eventLoop.start();
//        eventLoop.join();

        var handler = new BasicHandler();

        var options = Options.builder().withPort(5523).build();
        EventLoop eventLoop = new EventLoop(options, handler);

        eventLoop.start();
        eventLoop.join(); // don't stop

    }
}

