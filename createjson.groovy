@Grab("net.sf.opencsv:opencsv:2.3")
@Grab("org.codehaus.groovy.modules.http-builder:http-builder:0.5.2")
import au.com.bytecode.opencsv.CSVReader
import groovyx.net.http.*
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

def readFile(String filename) {
	char separator = '\t'
	CSVReader reader = new CSVReader(new FileReader(filename), separator)
	String[] headers = reader.readNext()
	List<String[]> entries = reader.readAll()
	places = []
	entries.collect { line ->
		int i = 0
		values = [:]
		headers.collect { header ->
			if (i < line.length) {
				values[header] = line[i]
			}
			i++
		}
		location = ["lat": values.Lat, "lon": values.Lon]
		values["location"] = location
		values.remove("Lat")
		values.remove("Lon")
		places << values
	}
	places
}

def putDocuments(data) {
	url = "http://localhost:9200/weather/place/"
	http = new HTTPBuilder(url)
	data.each { doc ->
		http.request(POST, JSON) { req ->
			body = doc
			
			response.success = {resp, json ->
				println json
			}
		}
		
	}
}

places = readFile(args[0])
putDocuments(places)