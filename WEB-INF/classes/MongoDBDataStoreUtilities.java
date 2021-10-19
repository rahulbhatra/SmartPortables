import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.AggregationOutput;

import java.util.*;

public class MongoDBDataStoreUtilities {
    static DBCollection myReviews;

    public static DBCollection getConnection() {
        MongoClient mongo;
        mongo = new MongoClient("localhost", 27017);

        DB db = mongo.getDB("CustomerReviews");
        myReviews = db.getCollection("myReviews");
        return myReviews;
    }


    public static boolean insertReview(Long productId, String productName, String productCategory, double productPrice,
                                       String productManufacturer, boolean productIsOnSale, Long userId, String userName, double userAge,
                                       String userGender, String userOccupation, Long storeLocationId,
                                       String storeStreetAddress, String storeCity, String storeState, String storeZipCode,
                                       Integer reviewRating, String reviewDate, String reviewText, String retailerZipCode, String retailerCity) {
        try {
            getConnection();
            BasicDBObject doc = new BasicDBObject("title", "myReviews").
                    append("productId", productId).
                    append("productName", productName).
                    append("productCategory", productCategory).
                    append("productPrice", productPrice).
                    append("productManufacturer", productManufacturer).
                    append("productIsOnSale", productIsOnSale).
                    append("userId", userId).
                    append("userName", userName).
                    append("userAge", userAge).
                    append("userGender", userGender).
                    append("userOccupation", userOccupation).
                    append("storeLocationId", storeLocationId).
                    append("storeStreetAddress", storeStreetAddress).
                    append("storeCity", storeCity).
                    append("storeState", storeState).
                    append("storeZipCode", storeZipCode).
                    append("reviewRating", reviewRating).
                    append("reviewDate", reviewDate).
                    append("reviewText", reviewText).
                    append("retailerZipCode", retailerZipCode).
                    append("retailerCity", retailerCity);

            myReviews.insert(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static HashMap<Long, ArrayList<Review>> selectReview() {
        HashMap<Long, ArrayList<Review>> reviews;

        try {

            getConnection();
            DBCursor cursor = myReviews.find();
            reviews = new HashMap<>();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();

                if (!reviews.containsKey(obj.getLong("productId"))) {
                    ArrayList<Review> arr = new ArrayList<Review>();
                    reviews.put(obj.getLong("productId"), arr);
                }
                ArrayList<Review> listReview = reviews.get(obj.getLong("productId"));
                Review review = new Review(obj.getLong("productId"), obj.getString("productName"),
                        obj.getString("productCategory"), obj.getDouble("productPrice"),
                        obj.getString("productManufacturer"), obj.getBoolean("productIsOnSale"),
                        obj.getLong("userId"), obj.getString("userName"),
                        obj.getDouble("userAge"), obj.getString("userGender"),
                        obj.getString("userOccupation"), obj.getLong("storeLocationId"),
                        obj.getString("storeStreetAddress"), obj.getString("storeCity"),
                        obj.getString("storeState"), obj.getString("storeZipCode"),
                        obj.getInt("reviewRating"), obj.getString("reviewDate"),
                        obj.getString("reviewText"), obj.getString("retailerZipCode"),
                        obj.getString("retailerCity"));
                listReview.add(review);
            }
            return reviews;
        } catch (Exception e) {
            reviews = null;
            e.printStackTrace();
            return reviews;
        }


    }


    public static ArrayList<BestRating> topProducts() {
        ArrayList<BestRating> bestRate = new ArrayList<>();
        try {

            getConnection();
            int retlimit = 5;
            DBObject sort = new BasicDBObject();
            sort.put("reviewRating", -1);
            DBCursor cursor = myReviews.find().limit(retlimit).sort(sort);
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                System.out.println("------------- Top Products -------" + obj.toJson().toString());
                Long productId = obj.getLong("productId");
                String rating = obj.get("reviewRating").toString();
                BestRating best = new BestRating(productId, rating);
                bestRate.add(best);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return bestRate;
    }

    public static ArrayList<Mostsoldzip> mostSoldZip() {
        ArrayList<Mostsoldzip> mostSoldZips = new ArrayList<>();
        try {

            getConnection();
            DBObject groupProducts = new BasicDBObject("_id", "$retailerZipCode");
            groupProducts.put("count", new BasicDBObject("$sum", 1));
            DBObject group = new BasicDBObject("$group", groupProducts);
            DBObject limit = new BasicDBObject("$limit", 5);

            DBObject sortFields = new BasicDBObject("count", -1);
            DBObject sort = new BasicDBObject("$sort", sortFields);
            AggregationOutput output = myReviews.aggregate(group, sort, limit);
            for (DBObject res : output.results()) {
                String zipcode = (res.get("_id")).toString();
                String count = (res.get("count")).toString();
                mostSoldZips.add(new Mostsoldzip(zipcode, count));
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return mostSoldZips;
    }

    public static ArrayList<MostSold> mostSoldProducts() {
        ArrayList<MostSold> mostSolds = new ArrayList<MostSold>();
        try {


            getConnection();
            DBObject groupProducts = new BasicDBObject("_id", "$productId");
            groupProducts.put("count", new BasicDBObject("$sum", 1));
            DBObject group = new BasicDBObject("$group", groupProducts);
            DBObject limit = new BasicDBObject("$limit", 5);

            DBObject sortFields = new BasicDBObject("count", -1);
            DBObject sort = new BasicDBObject("$sort", sortFields);
            AggregationOutput output = myReviews.aggregate(group, sort, limit);

            for (DBObject res : output.results()) {
                Long productId = Long.parseLong(res.get("_id").toString());
                String count = (res.get("count")).toString();
                MostSold mostSold = new MostSold(productId, count);
                mostSolds.add(mostSold);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return mostSolds;
    }

    public static ArrayList<Review> selectReviewForChart() {

        ArrayList<Review> reviewList = new ArrayList<>();
        try {
            getConnection();
            Map<String, Object> dbObjIdMap = new HashMap<>();
            dbObjIdMap.put("retailerZipCode", "$retailerZipCode");
            dbObjIdMap.put("productId", "$productId");
            DBObject groupFields = new BasicDBObject("_id", new BasicDBObject(dbObjIdMap));
            groupFields.put("count", new BasicDBObject("$sum", 1));
            DBObject group = new BasicDBObject("$group", groupFields);

            DBObject projectFields = new BasicDBObject("_id", 0);
            projectFields.put("retailerZipCode", "$_id");
            projectFields.put("productId", "$productId");
            projectFields.put("reviewCount", "$count");
            DBObject project = new BasicDBObject("$project", projectFields);

            DBObject sort = new BasicDBObject();
            sort.put("reviewCount", -1);

            DBObject orderby = new BasicDBObject("$sort", sort);


            AggregationOutput aggregate = myReviews.aggregate(group, project, orderby);

            for (DBObject result : aggregate.results()) {
                System.out.println(result.toString());
                BasicDBObject obj = (BasicDBObject) result;

                BasicDBObject retailerZipCodeObj = (BasicDBObject) obj.get("retailerZipCode");


                Review review = new Review(retailerZipCodeObj.getLong("productId"),
                        obj.getInt("reviewCount"), retailerZipCodeObj.getString("retailerZipCode"));
                System.out.println("---------- Review -----" + review.toString());
                Product product = MySqlDataStoreUtilities.getProduct(review.getProductId());
                review.setProductName(product.getProductName());
                reviewList.add(review);

            }
            return reviewList;

        } catch (Exception e) {
            reviewList = null;
            e.printStackTrace();
            return reviewList;
        }

    }


}	