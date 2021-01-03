package controllers;


import models.Product;
import repositories.ProductRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link play.libs.concurrent.HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class ProductController extends Controller {

    private final FormFactory formFactory;
    private final ProductRepository productRepository;
    private final HttpExecutionContext ec;

    @Inject
    public ProductController(FormFactory formFactory, ProductRepository productRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.productRepository = productRepository;
        this.ec = ec;
    }

    /*
    POST  /product/create

        {
            "name": "iPhone 1",
            "price": 4000
        }



     */

    public CompletionStage<Result> addProduct(final Http.Request request) {
        Product product = formFactory.form(Product.class).bindFromRequest(request).get();
        return productRepository
                .add(product)
                .thenApplyAsync(p -> ok(toJson(p)), ec.current());
    }


    // /products

    public CompletionStage<Result> getProducts() {
        return productRepository
                .list()
                .thenApplyAsync(productStream -> ok(toJson(productStream.collect(Collectors.toList()))), ec.current());
    }

}