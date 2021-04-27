//package ChitChat.controller;
//
//import graphql.ExecutionResult;
//import graphql.GraphQL;
//import graphql.schema.DataFetcher;
//import graphql.schema.GraphQLSchema;
//import graphql.schema.idl.RuntimeWiring;
//import graphql.schema.idl.SchemaGenerator;
//import graphql.schema.idl.SchemaParser;
//import graphql.schema.idl.TypeDefinitionRegistry;
//import ChitChat.model.User;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ChitChat.repository.UserFirestoreIO;
//
//import javax.annotation.PostConstruct;
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.ExecutionException;
//
//@RestController
//@CrossOrigin(origins = "*")
////@RequestMapping("/api")
//public class UserController {
//
//    // injecting value of schema,graphql to graphQlSchema
//
//    private Resource graphQlSchema;
//
//    private GraphQL graphQl;
//
//    @PostConstruct //run after app initiation
//    public void loadSchema() throws IOException {
//        File schemaFile = graphQlSchema.getFile();
//        // parse schema
//        TypeDefinitionRegistry parsedSchemaFile = new SchemaParser().parse(schemaFile);
//        RuntimeWiring wiring = wireSchemaWithFunctions();
//        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(parsedSchemaFile, wiring);
//        graphQl = GraphQL.newGraphQL(schema).build();
//    }
//
//    private RuntimeWiring wireSchemaWithFunctions(){
//        DataFetcher<User> fetcher1 = data -> UserFirestoreIO.getUser(data.getArgument("userId"));
//
//        return RuntimeWiring.newRuntimeWiring().type("Query", typeWriting -> typeWriting.dataFetcher("getUser", fetcher1)).build();
//    }
//
//
//
////    @RequestMapping("/")
////    public String index(){
////        return "hi";
////    }
//
//    @GetMapping("/api")
//    public User getUserById(@RequestBody String userId) throws ExecutionException, InterruptedException {
//        return UserFirestoreIO.getUser(userId);
//    }
//
//    /// GraphQL domains
//    @PostMapping("/findUserByIdQL")
//    public ResponseEntity<Object> findUserById(@RequestBody String query){
//        ExecutionResult result = graphQl.execute(query);
//        return new ResponseEntity<Object>(result, HttpStatus.OK);
//    }
//}
