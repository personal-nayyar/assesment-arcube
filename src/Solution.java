import java.util.*;

public class Solution {
    static class Offer{
//        OFFER_ID ITINERARY_ID PROVIDER_CODE PRICE CURRENCY STOPS TOTAL_DURATION, INCLUDES_BAG REFUNDABLE
        String offerId, itineraryId, provideCode, currency;
        double price;
        int stops, totalDuration;
        boolean includesBag, refundable;

        public Offer(String offerId, String itineraryId, String provideCode, String currency, double price, int stops, int totalDuration, boolean includesBag, boolean refundable) {
            this.offerId = offerId;
            this.itineraryId = itineraryId;
            this.provideCode = provideCode;
            this.currency = currency;
            this.price = price;
            this.stops = stops;
            this.totalDuration = totalDuration;
            this.includesBag = includesBag;
            this.refundable = refundable;
        }
    }
    static  class SearchCriteria{
//        CURRENCY MAX_STOPS MAX_TOTAL_DURATION REQUIRE_BAG
        String currency;
        int maxStops, maxTotalDuration;
        boolean requireBag;

        public SearchCriteria(String currency, int maxStops, int maxTotalDuration, boolean requireBag) {
            this.currency = currency;
            this.maxStops = maxStops;
            this.maxTotalDuration = maxTotalDuration;
            this.requireBag = requireBag;
        }
    }

    public static void main(String[] args) {
        List<Offer> offers = Arrays.asList(
                new Offer("OFF001", "ITN100", "6E","EUR", 150.00 ,1, 320, true, false),
                new Offer("OFF002", "ITN100", "MMT","EUR", 150.00 ,1, 300, true, true),
                new Offer("OFF003", "ITN100", "EK","EUR", 200.00 ,0, 280, true, true),
                new Offer("OFF004", "ITN200", "LH","EUR", 180.00 ,1, 400, false, false),
                new Offer("OFF005", "ITN200", "AI","EUR", 190.00 ,1, 350, true, false)
                );

        SearchCriteria criteria = new SearchCriteria("EUR", 2 ,600, true);
        bestOffer(offers, criteria);

        List<Offer> offers2 = Arrays.asList(
                new Offer("OFF001", "ITN100", "6E","UDS", 150.00 ,1, 320, true, false),
                new Offer("OFF002", "ITN100", "MMT","EUR", 0 ,1, 300, true, true)
        );

        SearchCriteria criteria2 = new SearchCriteria("EUR", 2 ,600, true);
        bestOffer(offers2, criteria2); // no output
    }

    public static void bestOffer(List<Offer> offers, SearchCriteria criteria){
        Map<String, List<Offer>> itineraryOffer = new HashMap<>();
        // filter
        for (Offer offer: offers){
            if (isValidaOffer(offer, criteria)){
                itineraryOffer.computeIfAbsent(offer.itineraryId, k -> new ArrayList<>()).add(offer);
            }
        }

        // select best offer
        TreeMap<String, String> results = new TreeMap<>();
        for (Map.Entry<String, List<Offer>> entry: itineraryOffer.entrySet()){
            String itId = entry.getKey();
            List<Offer> offerslist = entry.getValue();

            // find best among offer
            Offer bestOffer = offerslist.get(0);
            for (int i = 1; i < offerslist.size(); i++) {
                Offer currOffer = offerslist.get(i);
                if (isBetterOffer(currOffer, bestOffer)){
                    bestOffer = currOffer;
                }
            }
            results.put(itId, bestOffer.offerId);
        }

        // print
        for(Map.Entry<String, String> res: results.entrySet()){
            System.out.println(res.getKey() +" "+ res.getValue());
        }
    }

    private static boolean isBetterOffer(Offer a, Offer b){
        // price
        int cmp = Double.compare(a.price, b.price);
        if (cmp < 0)
            return true;
        if (cmp > 0)
            return false;

        if (a.includesBag != b.includesBag)
            return a.includesBag;

        if (a.refundable != b.refundable)
            return a.refundable;

        if (a.totalDuration < b.totalDuration) return true;
        if (a.totalDuration > b.totalDuration) return false;

        return a.provideCode.compareTo(b.provideCode) < 0;
    }

    private static boolean isValidaOffer(Offer offer, SearchCriteria criteria){
        return !offer.offerId.isEmpty() && !offer.itineraryId.isEmpty()
                && !offer.currency.isEmpty() && offer.currency.equals(criteria.currency)
                && offer.price > 0
                && offer.stops <= criteria.maxStops && offer.totalDuration <= criteria.maxTotalDuration
                && (!criteria.requireBag || offer.includesBag);
    }



}
