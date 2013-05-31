@Grab("net.sf.opencsv:opencsv:2.3")
@Grab("org.elasticsearch:elasticsearch:0.90.1")
@Grap("org.codehaus.groovy.modules.http-builder:http-builder:0.5.2")
import au.com.bytecode.opencsv.CSVReader
import groovyx.net.http.*


def readFile(String filename) {
	char separator = '\t'
	CSVReader reader = new CSVReader(new FileReader(filename), separator)
	String[] headers = reader.readNext()
	List<String[]> entries = reader.readAll()
	places = []
	values = [:]
	entries.collect { line ->
		int i = 0
		headers.collect { header ->
			if (i < line.length) {
				values[header] = line[i]
			}
			i++
		}
		places << values
	}
	places
}

def putDocuments(data) {
	url = "http://localhost:9200/weather/place/"
	http = new HTTPBuilder(url)
	data.each { doc ->
		http.request(POST, json) { req ->
			body = doc
			
			response.success = {resp, json ->
				println json
			}
		}
		
	}
}

places = readFile(args[0])
putDocuments(places)