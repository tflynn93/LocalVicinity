package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/4/2015.
 */

public class QueryBuilder {

    /**
     * Specify your database name here
     *
     * @return
     */
    public String getDatabaseName() {
        return "localvicinity";
    }

    /**
     * Specify your MongoLab API here
     *
     * @return
     */
    public String getApiKey() {
        return "tmmc1B00d-f8_7S6NFd3nDSvvGjfkKQf";
    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     *
     * @return
     */
    public String getBaseUrl() {
        return "https://api.mongolab.com/api/1/databases/" + getDatabaseName() + "/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     *
     * @return
     */
    public String docApiKeyUrl() {
        return "?apiKey=" + getApiKey();
    }

    /**
     * Get a specified document
     *
     * @param docid
     * @return
     */
    public String docApiKeyUrl(String docid) {
        return "/" + docid + "?apiKey=" + getApiKey();
    }

    /**
     * Returns the docs101 collection
     *
     * @return
     */
    public String documentRequest() {
        return "locations";
    }

    /**
     * Builds a complete URL using the methods specified above
     *
     * @return
     */
    public String buildLocationSaveURL() {
        return getBaseUrl() + documentRequest() + docApiKeyUrl();
    }

    /**
     * This method is identical to the one above.
     *
     * @return
     */
    public String buildLocationGetURL() {
        return getBaseUrl() + documentRequest() + docApiKeyUrl();
    }

    /**
     * Get a Mongodb document that corresponds to the given object id
     *
     * @param doc_id
     * @return
     */
    public String buildLocationUpdateURL(String doc_id) {
        return getBaseUrl() + documentRequest() + docApiKeyUrl(doc_id);
    }


    /**
     * Formats the location details for MongoHLab Posting
     *
     * @param myLocation: Details of the person
     * @return
     */
    public String createLocation(MyLocation myLocation) {
        return String
                .format("{\"name\": \"%s\", "
                                + "\"longitude\": \"%s\", \"latitude\": \"%s\", "
                                + "\"type\": \"%s\"}",
                        myLocation.name, myLocation.longitude, myLocation.latitude, myLocation.location_type);
    }

    /**
     * Update a given location record
     *
     * @param myLocation
     * @return
     */
    public String setLocationData(MyLocation myLocation) {
        return String.format("{ \"$set\" : "
                        + "{\"name\" : \"%s\", "
                        + "\"longitude\" : \"%s\", "
                        + "\"latitude\" : \"%s\", "
                        + "\"type\" : \"%s\", "
                        + "\"incorrect\" : \"%s\" }" + "}",
                myLocation.getName(),
                myLocation.getLongitude(), myLocation.getLatitude(),
                myLocation.getType(),
                myLocation.getFlag());
    }

}