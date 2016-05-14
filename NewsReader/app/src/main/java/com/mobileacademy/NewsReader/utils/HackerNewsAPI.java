package com.mobileacademy.NewsReader.utils;

/**
 * Created by cornelbalaban on 06/04/16.
 */
public final class HackerNewsAPI {

    private static final String BASE_ENDPOINT= "https://hacker-news.firebaseio.com/v0/";
    private static final String TOP_STORIES_ENDPOINT= BASE_ENDPOINT + "topstories.json";
    private static final String NEW_STORIES_ENDPOINT= BASE_ENDPOINT + "newstories.json";
    private static final String ITEM_ENDPOINT= BASE_ENDPOINT + "item/";


    /**
     *
     * @param urlPurpose is the url that should be returned: topStories, newStories
     * @return  URLs or the BASE URL as default
     */
    public static String getEndpointUrl(String urlPurpose) {

        String returnUrl;

        switch (urlPurpose) {
            case "topStories":
                returnUrl =  TOP_STORIES_ENDPOINT;
                break;
            case "newStories":
                returnUrl = NEW_STORIES_ENDPOINT;
                break;
//            case "itemDescription":
//                returnUrl = ITEM_ENDPOINT;
//                break;
            default:
                returnUrl = BASE_ENDPOINT;
                break;
        }
        return returnUrl;
    }

    public static String getItemUrl (String id)
    {
        return ITEM_ENDPOINT + id + ".json";
    }
}
