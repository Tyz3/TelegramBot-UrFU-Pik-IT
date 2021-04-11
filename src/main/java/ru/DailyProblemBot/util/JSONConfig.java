package ru.DailyProblemBot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.GsonBuilder;

@SuppressWarnings("unchecked")
public class JSONConfig {
	
	protected Map<String, Object> object;
	
	private final File jsonFile;
	private boolean compressJson = false;
	private int capacityListElements = 50;
	
	public String getName() {
		return jsonFile.getName();
	}

	/**
	 * @param fileName название файла, который был создан или будет.
	 * @param path названия папки, в которой имеется файл или будет создан с названием fileName.
	 */
	public JSONConfig(String fileName, String path) {
		this(path.concat(File.separator).concat(fileName));
	}

	/**
	 * @param absolutePath название файла, который был создан или будет.
	 */
	public JSONConfig(String absolutePath) {
		jsonFile = new File(absolutePath);
		loadJsonFile();

		initializeMap();
		loadDataToRAM();
	}
	
	/**
	 * @param fileName название файла, который был создан или будет.
	 * @param path названия папки, в которой имеется файл или будет создан с названием fileName.
	 * @param compressJson стоит убрать отступы и переносы для уменьшения веса файла.
	 */
	public JSONConfig(String fileName, String path, boolean compressJson) {
		this(fileName, path);
		this.compressJson = compressJson;
	}
	
	/**
	 * @param fileName название файла, который был создан или будет.
	 * @param path названия папки, в которой имеется файл или будет создан с названием fileName.
	 * @param compressJson стоит убрать отступы и переносы для уменьшения веса файла.
	 */
	public JSONConfig(String fileName, String path, boolean compressJson, int capacityListElements) {
		this(fileName, path, compressJson);
		this.capacityListElements = capacityListElements;
	}
	
	private void loadJsonFile() {
		if (!jsonFile.getParentFile().exists()) {
			jsonFile.getParentFile().mkdirs();
        }

		if (!jsonFile.exists() || jsonFile.length() < 2) {
			PrintWriter pw;
			try {
				pw = new PrintWriter(jsonFile, StandardCharsets.UTF_8);
				pw.print("{}");
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void initializeMap() {
		object = new HashMap<>();
	}
	
	private void loadDataToRAM() {
		FileReader fr;
		try {
			fr = new FileReader(jsonFile);
			JSONObject object = (JSONObject) new JSONParser().parse(fr);
			Set<Entry<String, Object>> o = object.entrySet();
			for (Entry<String, Object> e : o) {
				this.object.put(e.getKey(), e.getValue());
			}
	    	fr.close();
		} catch (IOException | ParseException e) {
			System.out.println("JSONObject for "+jsonFile.getName()+" load error with IOException or ParseException.");
			e.printStackTrace();
		}
	}

	public void clear() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(jsonFile, StandardCharsets.UTF_8);
			pw.print("{}");
			pw.flush();
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("JsonFile for "+jsonFile.getName()+" load error with FileNotFound or UnsuppEncoding.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadDataToRAM();
	}
	
	public Set<String> keySet() {
		return object.keySet();
	}
	
	public Set<String> keySet(String path) {
		Map<String, Object> hm = getMap(path);
		return hm == null ? null : hm.keySet();
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 * @param object значение, которое будет вставлено по указанному пути.
	 */
	public void set(String path, Object object) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = this.object;
			
			for (int i = 0; i < positions.length - 1; i++) {

				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {

					map = (HashMap<String, Object>) obj;

				} else if (obj instanceof Collection<?> || obj instanceof Long || obj instanceof Double || obj instanceof Boolean || obj instanceof String) {

					map.put(positions[i], new HashMap<String, Object>());
					map = (HashMap<String, Object>) map.get(positions[i]);

				} else if (obj == null) {

					map.put(positions[i], new HashMap<String, Object>());
					map = (HashMap<String, Object>) map.get(positions[i]);

					if (i == positions.length - 2) {
						if (object == null) map.remove(positions[i+1]);
						else map.put(positions[i+1], object);
					}

					continue;
				}
				
				if (i == positions.length - 2) {

					if (object == null) {
						map.remove(positions[i+1]);
					} else {
						map.put(positions[i + 1], object);
					}

				}
			}

		} else {

			if (object == null) {
				this.object.remove(path);
			} else {
				this.object.put(path, object);
			}

		}
	}
	
	/**
	 * Сохранение JSON объекта в файл на память компьютера.
	 * @return Возвращает состояние сохранения.
	 */
	public boolean save() {
		JSONObject obj = new JSONObject();
		obj.putAll(this.object);
		
    	try {
            String jsonForWrite = (compressJson) ? obj.toJSONString() : new GsonBuilder().setPrettyPrinting().create().toJson(obj);
            
            FileWriter outfile = new FileWriter(jsonFile, false);
            outfile.write(jsonForWrite);
            outfile.flush();
            outfile.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Object get(String path) {
		String[] positions = path.split("\\.");

		if (positions.length != 1) {
			Map<String, Object> map = object;

			for (int i = 0; i < positions.length - 1; i++) {
				Object obj = map.get(positions[i]);

				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					return null;
				} else if (i != positions.length - 2) {
					return null;
				}

				if (i == positions.length - 2) {
					return map.get(positions[i + 1]);
				}
			}
		} else {
			return object.get(path);
		}

		return null;
	}
	
	public String getString(String path, String defValue) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					return defValue;
				} else if ((obj instanceof Collection<?> || obj instanceof Double || obj instanceof Boolean || obj instanceof Long) && i != positions.length-2) {
					return defValue;
				}
				
				if (i == positions.length-2) {
					Object obj2 = map.get(positions[i+1]);
					return obj2 instanceof String ? obj2.toString() : defValue;
				}
			}
		} else {
			Object obj = object.get(path);
			return obj instanceof String ? obj.toString() : defValue;
		}
		
		return defValue;
	}
	
	public int getInt(String path, long defValue) {
		return (int) getLong(path, defValue);
	}
	
	public long getLong(String path, long defValue) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					return defValue;
				} else if ((obj instanceof Collection<?> || obj instanceof Double || obj instanceof Boolean || obj instanceof String) && i != positions.length-2) {
					return defValue;
				}
				
				if (i == positions.length-2) {
					Object obj2 = map.get(positions[i+1]);
					return obj2 instanceof Long ? (long) obj2 : obj2 instanceof Integer ? (int) obj2 : defValue;
				}
			}
		} else {
			Object obj = object.get(path);
			return obj instanceof Long ? (long) obj : obj instanceof Integer ? (int) obj : defValue;
		}
		
		return defValue;
	}
	
	public double getDouble(String path, double defValue) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					return defValue;
				} else if ((obj instanceof Collection<?> || obj instanceof Long || obj instanceof Boolean || obj instanceof String) && i != positions.length-2) {
					return defValue;
				}
				
				if (i == positions.length-2) {
					Object obj2 = map.get(positions[i+1]);
					return obj2 instanceof Double ? (double) obj2 : defValue;
				}
			}
		} else {
			Object obj = object.get(path);
			return obj instanceof Double ? (double) obj : defValue;
		}
		
		return defValue;
	}
	
	public boolean getBoolean(String path, boolean defValue) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					return defValue;
				} else if ((obj instanceof Collection<?> || obj instanceof Long || obj instanceof Double || obj instanceof String) && i != positions.length-2) {
					return defValue;
				}
				
				if (i == positions.length-2) {
					Object obj2 = map.get(positions[i+1]);
					return obj2 instanceof Boolean ? (boolean) obj2 : defValue;
				}
			}
		} else {
			Object obj = object.get(path);
			return obj instanceof Boolean ? (boolean) obj : defValue;
		}
		
		return defValue;
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 */
	public void addKeyValueToArray(String path, String key, String value) {
		List<JSONObject> list = getJSONObjectsArray(path);
		addKeyValueToArray(path, key, value, -1);
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 * @param value строка для конвертации в JSONObject в виде "key=value".
	 * @param position позиция добавляемого объекта (указать -1, если нужно добавить в конец).
	 */
	public void addKeyValueToArray(String path, String key, String value, int position) {
		List<JSONObject> list = getJSONObjectsArray(path);
		if (list == null) list = new ArrayList<>();

		HashMap<String, String> hm = new HashMap<>();
		hm.put(key, value);

		JSONObject obj = new JSONObject();
		obj.putAll(hm);

		if (position != -1)
			list.add(position, obj);
		else
			list.add(obj);

		// Очистка лишних записей
		if (list.size() > capacityListElements)
			list.subList(capacityListElements, list.size()).clear();
//			for (int i = list.size()-1; i >= capacityListElements; i--) list.remove(i);
	}
	
	/**
	 * @param objs список JSON объектов.
	 * @return Возвращает преобразованный список JSON объектов в список String объектов
	 * в виде "key=value".
	 */
	public static List<String> JSONObjectListToStringList(List<JSONObject> objs) {
		List<String> list = new ArrayList<>();
		for (JSONObject jo : objs) {
			Set<Entry<String, Object>> set = jo.entrySet();
			set.forEach(e -> list.add(e.getKey()+"="+e.getValue()));
		}
		
		return list;
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 * @param value добавляемый объект тип String.
	 */
	public void addObjectToList(String path, Object value) {
		List<Object> list = getObjectList(path);
		if (list == null) list = new ArrayList<>();
		list.add(value);
		
		// Очистка лишних записей
		if (list.size() > capacityListElements)
			list.subList(capacityListElements, list.size()).clear();
//			for (int i = list.size()-1; i >= capacityListElements; i--) list.remove(i);
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 * @param value добавляемый объект типа Object.
	 * @param position позиция в списке добавляемого элемента.
	 */
	public void addObjectToList(String path, Object value, int position) {
		List<Object> list = getObjectList(path);
		if (list == null) list = new ArrayList<>();
		list.add(position, value);
		
		// Очистка лишних записей
		if (list.size() > capacityListElements)
			list.subList(capacityListElements, list.size()).clear();
//			for (int i = list.size()-1; i >= capacityListElements; i--) list.remove(i);
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 * @param position позиция в списке удаляемого элемента.
	 */
	public void removeObjectFromList(String path, int position) {
		List<Object> list = getObjectList(path);
		if (list == null) return;
		list.remove(position);
	}
	
	public List<String> getStringList(String path) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					HashMap<String, Object> hm = new HashMap<>();
					map.put(positions[i], hm);
					map = hm;
				} else if ((obj instanceof Boolean || obj instanceof Long || obj instanceof Double || obj instanceof String) && i != positions.length-2) {
					return null;
				}
				
				if (i == positions.length-2) {
					Object obj2 = map.get(positions[i+1]);
					if (obj2 == null) {
						List<String> list = new ArrayList<>();
						map.put(positions[i+1], list);
						return list;
					}
					return obj2 instanceof Collection<?> ? (List<String>) obj2 : null;
				}
			}
		} else {
			Object obj = object.get(path);
			if (obj == null) {
				List<String> list = new ArrayList<>();
				object.put(path, list);
				return list;
			}
			return obj instanceof Collection<?> ? (List<String>) obj : null;
		}
		
		return null;
	}
	
	public List<Long> getLongList(String path) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					HashMap<String, Object> hm = new HashMap<>();
					map.put(positions[i], hm);
					map = hm;
				} else if ((obj instanceof Boolean || obj instanceof Long || obj instanceof Double || obj instanceof String) && i != positions.length-2) {
					return null;
				}
				
				if (i == positions.length-2) {
					Object obj2 = map.get(positions[i+1]);
					if (obj2 == null) {
						List<Long> list = new ArrayList<>();
						map.put(positions[i+1], list);
						return list;
					}
					return obj2 instanceof Collection<?> ? (List<Long>) obj2 : null;
				}
			}
		} else {
			Object obj = object.get(path);
			if (obj == null) {
				List<Long> list = new ArrayList<>();
				object.put(path, list);
				return list;
			}
			return obj instanceof Collection<?> ? (List<Long>) obj : null;
		}
		
		return null;
	}
	
	public List<Object> getObjectList(String path) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					HashMap<String, Object> hm = new HashMap<>();
					map.put(positions[i], hm);
					map = hm;
				} else if ((obj instanceof Boolean || obj instanceof Long || obj instanceof Double || obj instanceof String) && i != positions.length-2) {
					return null;
				}
				
				if (i == positions.length-2) {
					Object obj2 = map.get(positions[i+1]);
					if (obj2 == null) {
						List<Object> list = new ArrayList<>();
						map.put(positions[i+1], list);
						return list;
					}
					return obj2 instanceof Collection<?> ? (List<Object>) obj2 : null;
				}
			}
		} else {
			Object obj = object.get(path);
			if (obj == null) {
				List<Object> list = new ArrayList<>();
				object.put(path, list);
				return list;
			}
			return obj instanceof Collection<?> ? (List<Object>) obj : null;
		}
		
		return null;
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 * @return Возвращает список JSON объктов из списка JSONArray.
	 */
	public List<JSONObject> getJSONObjectsArray(String path) {
		String[] positions = path.split("\\.");
		
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (HashMap<String, Object>) obj;
				} else if (obj == null) {
					HashMap<String, Object> hm = new HashMap<>();
					map.put(positions[i], hm);
					map = hm;
				} else if ((obj instanceof Boolean || obj instanceof Long || obj instanceof Double || obj instanceof String) && i != positions.length-2) {
					return null;
				}
				
				if (i == positions.length-2) {
					Object obj2 = map.get(positions[i+1]);
					if (obj2 == null) {
						List<JSONObject> list = new ArrayList<>();
						map.put(positions[i+1], list);
						return list;
					}
					return obj2 instanceof Collection<?> ? (List<JSONObject>) obj2 : null;
				}
			}
		} else {
			Object obj = object.get(path);
			if (obj == null) {
				List<JSONObject> list = new ArrayList<>();
				object.put(path, list);
				return list;
			}
			return obj instanceof Collection<?> ? (List<JSONObject>) obj : null;
		}
		
		return null;
	}
	
	public Map<String, Integer> getIntMap(String path) {
		Map<String, Object> hm = getMap(path);
		if (hm == null) return new HashMap<>();
		Map<String, Integer> ihm = new HashMap<>();
		for (Entry<String, Object> entry : hm.entrySet()) {
			if (entry.getValue() instanceof Integer) {
				ihm.put(entry.getKey(), (Integer) entry.getValue());
			}
		}
		return ihm;
	}
	
	public Map<String, Long> getLongMap(String path) {
		Map<String, Object> hm = getMap(path);
		if (hm == null) return new HashMap<>();

		Map<String, Long> ihm = new HashMap<>();

		for (Entry<String, Object> entry : hm.entrySet()) {
			if (entry.getValue() instanceof Integer || entry.getValue() instanceof Long) {
				ihm.put(entry.getKey(), (Long) entry.getValue());
			}
		}

		return ihm;
	}
	
	public Map<String, Object> getMap(String path) {
		String[] positions = path.split("\\.");
		if (positions.length != 1) {
			Map<String, Object> map = object;
			
			for (int i = 0; i < positions.length-1; i++) {
				Object obj = map.get(positions[i]);
				
				if (obj instanceof HashMap<?, ?>) {
					map = (Map<String, Object>) obj;
				} else if (obj == null) {
					Map<String, Object> hm = new HashMap<>();
					map.put(positions[i], hm);
					map = hm;
				} else if ((obj instanceof Boolean || obj instanceof Long || obj instanceof Double || obj instanceof String || obj instanceof Collection<?>) && i != positions.length-2) {
					return null;
				}
				
				if (i == positions.length - 2) {
					Object obj2 = map.get(positions[i+1]);
					if (obj2 == null) {
						Map<String, Object> list = new HashMap<>();
						map.put(positions[i+1], list);
						return list;
					}
					return obj2 instanceof HashMap<?, ?> ? (HashMap<String, Object>) obj2 : null;
				}
			}
		} else {
			Object obj = object.get(path);
			if (obj == null) {
				HashMap<String, Object> list = new HashMap<>();
				object.put(path, list);
				return list;
			}
			return obj instanceof HashMap<?, ?> ? (HashMap<String, Object>) obj : null;
		}
		
		return null;
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 * @param value значение, на которое будет увеличено полученное число,
	 * если такового нет, то значение по умолчанию 0.
	 */
	public void inc(String path, long value) {
		long l = getLong(path, 0) + value;
		set(path, l);
	}
	
	/**
	 * @param path путь в JSON структуре, имеющий вид "object1.object2. и так далее".
	 * @param value значение, на которое будет увеличено полученное число,
	 * если такового нет, то берётся значение по умолчанию.
	 * @param defValue значение по умолчанию.
	 */
	public void increment(String path, long value, long defValue) {
		long l = getLong(path, defValue) + value;
		set(path, l);
	}
	
}
