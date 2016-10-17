import json

continents = ["Asia", "Europe", "Oceania", "Africa", "South America", "North America"]
rating = [1, 2, 3, 4, 5]
period = range(2010, 2016)

with open('data/json/countries.json') as countries_json:
	country_file = json.load(countries_json)

	for continent, countries in country_file.items():
		print(continent)
		print(countries)

file = []

