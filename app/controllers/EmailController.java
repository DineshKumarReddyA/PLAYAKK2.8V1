package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Email;
import play.mvc.*;
import play.*;
import play.libs.Json; // json handling, it uses Jackson
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class EmailController extends Controller   {
    List<Email> emails = new ArrayList<>();

    public EmailController() {
        Email email = new Email("Registeration done", "someone@example.com", "Your registration is successful" );
        Email email2 = new Email("Registeration done", "ab@example.com", "Your registration is successful" );

        emails.add(email);
        emails.add(email2);
    }

    public Result getEmail() {
        Email email = new Email("Registeration done", "someone@example.com", "Your registration is successful" );

        // JsonNode is generic JSON object
        JsonNode node = Json.toJson(email);
        return ok(node); // application/json
    }

    public Result getEmails() {
        // JsonNode is generic JSON object, array of json object
        JsonNode node = Json.toJson(emails);
        return ok(node); // application/json
    }

    // exclude some attributes from pojo
    public Result all() {
        ArrayNode result = Json.newArray();
        emails.forEach( email -> {
            // Specific Attributes
            ObjectNode node = Json.newObject();
            node.put("to", email.getTo());
            node.put("subject", email.getSubject());
            result.add(node);
        });

        return ok(result);
    }

    // option 1 to consume json and create pojo

    /*
     in postman, raw and Text, payload as follows

    {
    "to": "alex@example.com",
    "subject": "order placed",
    "body": "Your order successfully"
    }
     */
    public Result createEmailFromRawText(Http.Request request) {
        String raw = request.body().asText(); // JSON as text/plain
        System.out.println("*** " + raw);
        JsonNode emailNode = (JsonNode) Json.parse(raw);
        Email email = Json.fromJson(emailNode, Email.class); // JSON to POJO
        this.emails.add(email);

        return ok(Json.toJson(email));
    }

    /*
    option 2
   in postman, raw and Json, payload as follows, content-type shall be application/json

  {
  "to": "alex@example.com",
  "subject": "order placed",
  "body": "Your order successfully"
  }
   */
    public Result createEmailFromJson(Http.Request request) {
        // body function itself parsing data

        JsonNode emailNode = request.body().asJson(); // application/json type
        Email email = Json.fromJson(emailNode, Email.class); // JSON to POJO
        this.emails.add(email);

        return ok(Json.toJson(email));
    }


    /*
   option 2
  in postman, raw and Json, payload as follows, content-type shall be application/json

 {
 "to": "alex@example.com",
 "subject": "order placed",
 "body": "Your order successfully"
 }
  */

    // Option 3 using body parser
    @BodyParser.Of(BodyParser.Json.class)
    public Result createEmailUsingBP(Http.Request request) {
        JsonNode emailNode = request.body().asJson(); // application/json type

        Email email2 = request.body().as(Email.class);
        //Email email = Json.fromJson(emailNode, Email.class); // JSON to POJO
        //this.emails.add(email);

        String to = emailNode.findPath("to").textValue();
        String subject = emailNode.findPath("subject").textValue();
        String body = emailNode.findPath("body").textValue();

        Email email = new Email(subject, to, body);
        this.emails.add(email);

        return ok(Json.toJson(email));
    }

    // Handling collections

    // Helper
    public static <T> T fromJSON(final TypeReference<T> type,
                                 final String jsonPacket) {
        T data = null;

        try {
            data = new ObjectMapper().readValue(jsonPacket, type);
        } catch (Exception e) {
            // Handle the problem
        }
        return data;
    }

    /*

    type: text


    [
{
    "to": "alex@example.com",
    "subject": "order placed 2",
    "body": "Your order successfully"
},
{
    "to": "jo@example.com",
    "subject": "order placed 3",
    "body": "Your order successfully"
}
]



     */
    // array of emails as input
    public Result bulkMailsOM(Http.Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        Email[] emailsBulk = null;
        try {
            emailsBulk = objectMapper.readValue(request.body().asText(), Email[].class);
            for (Email email: emailsBulk) {
                emails.add(email);
            }
        }catch(Exception ex) {

        }

        return ok(Json.toJson(emailsBulk));
    }

}
