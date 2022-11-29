import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.swing.text.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class Datenbank {
    private String connectionString;
    private String databasename;
    private String collectionname;


    public Datenbank() {
        connectionString = "mongodb+srv://dbuser:x0nlXgyWtELMYtQS@cluster1.2m2xwhk.mongodb.net/?retryWrites=true&w=majority";
        databasename = "mongodbVSCodePlaygroundDB";
        collectionname = "privatbesitz";
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase(databasename);
        database.getCollection(collectionname).drop();
    }

    public void select(){
        try(MongoClient mongoClient = MongoClients.create(connectionString)){
            MongoDatabase database = mongoClient.getDatabase(databasename);
            MongoCollection<Document> collection = database.getCollection(collectionname);
            MongoCursor<Document> documents = collection.find().iterator();

            for(;documents.hasNext();) {
                String test = documents.next().toJson();
                System.out.println(test);
            }

        }catch(Exception e) {
            System.out.println("Connection failed!");
        }
    }

    public void selectSpecific(){
        try(MongoClient mongoClient = MongoClients.create(connectionString)){
            MongoDatabase database = mongoClient.getDatabase(databasename);
            MongoCollection<Document> collection = database.getCollection(collectionname);

            //eq erzeugt einen Filter
            MongoCursor<Document> documents = collection.find(eq("item", "computer")).iterator();

            for(;documents.hasNext();) {
                List<String> specific = new ArrayList<>();
                Document temp_doc = documents.next();
                var cars = new ArrayList<>(temp_doc.keySet());

                for (Map.Entry<String, Object> entry:temp_doc.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
            }

        }catch(Exception e) {
            System.out.println("Connection failed!");
        }
    }
    public void insert(List<Document> documents){
        try(MongoClient mongoClient = MongoClients.create(connectionString)){
            MongoDatabase database = mongoClient.getDatabase(databasename);
            InsertManyResult result = database.getCollection(collectionname).insertMany(documents);

        }catch(Exception e) {
            System.out.println("Connection failed!");
        }
    }

    public void delete(){
        try(MongoClient mongoClient = MongoClients.create(connectionString)){
            MongoDatabase database = mongoClient.getDatabase(databasename);
            DeleteResult result = database.getCollection(collectionname).deleteOne(eq("item", "computer"));
        }catch(Exception e) {
            System.out.println("Connection failed!");
        }
    }

    public void update() {
        try(MongoClient mongoClient = MongoClients.create(connectionString)){
            MongoDatabase database = mongoClient.getDatabase(databasename);
            Bson update = Updates.set("item", "laptop");
            UpdateResult result = database.getCollection(collectionname).updateMany(eq("item", "computer"), update);
            System.out.println(result);
        }catch(Exception e) {
            System.out.println("Connection failed!");
        }
    }

    public static void main(String[] args) {
        Document lambo = new Document()
                .append("item", "lamborghini")
                .append("modell", "urus")
                .append("baujahr", LocalDateTime.of(2022, 06, 04, 12, 0, 0))
                .append("price", 12000);

        Document computer = new Document()
                .append("item", "computer")
                .append("mainboard", new Document()
                        .append("name", "ASUS ROG Strix B550-F Gaming")
                        .append("formfaktor", "ATX")
                )
                .append("cpu", "AMD Ryzen 5 5600X");

        List<Document> documents = Arrays.asList(lambo, computer);

        Datenbank db = new Datenbank();
        db.insert(documents);
        db.select();
        db.selectSpecific();
        db.update();
        //db.delete();
    }


}
