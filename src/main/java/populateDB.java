import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.person.Address;
import com.devskiller.jfairy.producer.person.Person;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class populateDB {

	public static void populate(int N) {
		MongoClient client = new MongoClient("10.4.41.149");
		MongoDatabase database = client.getDatabase("lab_mongodb");
		MongoCollection<Document> personCollection = database.getCollection("person");
		MongoCollection<Document> companyCollection = database.getCollection("company");
		MongoCollection<Document> addressCollection = database.getCollection("address");


		for (int i = 0; i < N; i++) {
			Fairy fairy = Fairy.create();
			Person person = fairy.person();
			Company personsCompany = person.getCompany();
			Address personAddress = person.getAddress();

			String addressKey = personAddress.getStreet() + ":" + personAddress.getStreetNumber() + ":" + personAddress.getApartmentNumber();

			Document personDocument = new Document();
			personDocument.put("_id", person.getPassportNumber());

			if (personCollection.countDocuments(personDocument) == 0) {
				personDocument.put("age", person.getAge());
				personDocument.put("email", person.getEmail());
				personDocument.put("firstName", person.getFirstName());
				personDocument.put("lastName", person.getLastName());
				personDocument.put("countryCode", person.getNationality().getCode());
				personDocument.put("cityName", person.getAddress().getCity());

				personDocument.put("worksIn", personsCompany.getName());
				personDocument.put("livesIn", addressKey);

				personCollection.insertOne(personDocument);
			}

			Document addressDocument = new Document();
			addressDocument.put("_id", addressKey);
			if (companyCollection.countDocuments(addressDocument) == 0) {
				addressDocument.put("street", personAddress.getStreet());
				addressDocument.put("streetNumber", personAddress.getStreetNumber());
				addressDocument.put("apartmentNumber", personAddress.getApartmentNumber());
				addressCollection.insertOne(addressDocument);
			}

			Document companyDocument = new Document();
			companyDocument.put("_id", personsCompany.getName());
			if (companyCollection.countDocuments(companyDocument) == 0) {
				companyDocument.put("domain", personsCompany.getDomain());
				companyDocument.put("email", personsCompany.getEmail());
				companyDocument.put("url", personsCompany.getUrl());
				companyCollection.insertOne(companyDocument);
			}
		}
		client.close();
	}
}