package com.example.vitaminka;

import com.example.vitaminka.models.BatchResponse;
import com.example.vitaminka.models.Drug;
import com.example.vitaminka.models.DrugResponse;
import com.example.vitaminka.models.DrugType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static final String ADMIN_TOKEN = "\\ -H \"Content-Type: application/json\" \\\n" +
            "-d '{\"login\":\"admin\",\"password\":\"admin\"}' | jq -r \".token\"";

    // Универсальный GET-запрос, возвращает строку ответа
    public static String getResponse(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            throw new Exception("Response code: " + responseCode);
        }
    }

    // Загрузка корневых категорий (parentId = null)
    public static DrugType[] getRootCategories() throws Exception {
        String json = getResponse("api/v1/drugtypes?parentId=null" + ADMIN_TOKEN);
        JSONArray array = new JSONArray(json);
        DrugType[] result = new DrugType[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            int id = obj.getInt("id");
            String name = obj.getString("name");
            Integer parentId = obj.isNull("parentId") ? null : obj.getInt("parentId");
            result[i] = new DrugType(id, name, parentId);
        }
        return result;
    }

    // Загрузка подкатегорий по parentId
    public static DrugType[] getSubCategories(int parentId) throws Exception {
        String json = getResponse("api/v1/drugtypes?parentId=" + parentId + ADMIN_TOKEN);
        JSONArray array = new JSONArray(json);
        DrugType[] result = new DrugType[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            result[i] = new DrugType(obj.getInt("id"), obj.getString("name"), parentId);
        }
        return result;
    }

    // Загрузка товаров по drugTypeId
    public static Drug[] getDrugsByType(int drugTypeId) throws Exception {
        String json = getResponse("api/v1/drugs?TypeId=" + drugTypeId + ADMIN_TOKEN);
        JSONArray array = new JSONArray(json);
        Drug[] result = new Drug[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            result[i] = new Drug(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("description"),
                    obj.getInt("drugTypeId")
            );
        }
        return result;
    }

    public static List<DrugResponse> getDrugs() throws Exception {
        String json = getResponse("api/v1/drugs" + ADMIN_TOKEN);
        JSONArray array = new JSONArray(json);
        List<DrugResponse> drugs = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            DrugResponse drug = parseDrug(obj);
            drugs.add(drug);
        }
        return drugs;
    }

    private static DrugResponse parseDrug(JSONObject obj) throws Exception {
        DrugResponse drug = new DrugResponse();
        drug.id = obj.getInt("id");
        drug.name = obj.getString("name");
        drug.category = obj.getString("category");
        drug.inn = obj.getString("inn");
        drug.dose = obj.getString("dose");

        JSONObject manObj = obj.getJSONObject("manufacturer");
        drug.manufacturer = new DrugResponse.Manufacturer();
        drug.manufacturer.id = manObj.getInt("id");
        drug.manufacturer.name = manObj.getString("name");

        JSONObject typeObj = obj.getJSONObject("type");
        drug.type = new DrugResponse.Type();
        drug.type.id = typeObj.getInt("id");
        drug.type.name = typeObj.getString("name");

        return drug;
    }

    public static BatchResponse getBatchByDrugId(int drugId) throws Exception {
        String json = getResponse("api/v1/batches?drugId=" + drugId + ADMIN_TOKEN);
        // Предполагаем, что возвращается массив партий (возможно, одна)
        JSONArray array = new JSONArray(json);
        if (array.length() > 0) {
            JSONObject obj = array.getJSONObject(0);
            BatchResponse batch = new BatchResponse();
            batch.id = obj.getInt("id");
            batch.drugId = obj.getInt("drugId");
            batch.number = obj.getInt("number");
            batch.price = obj.getDouble("price");
            batch.shelfLife = obj.getString("shelfLife");
            return batch;
        }
        return null;
    }
}