import json
from pprint import pprint
import random as r

countries = {}

def fill_country_lists():
	target_eu = open("../data/txt/europe.txt", "r")
	eu_countries = []
	for line in target_eu:
		country = {}
		country["name"] = line.rstrip('\n')
		eu_countries.append(country)
	countries["Europe"] = eu_countries
	target_eu.close()

	target_na = open("../data/txt/na.txt", "r")
	na_countries = []
	for line in target_na:
		country = {}
		country["name"] = line.rstrip('\n')
		na_countries.append(country)
	countries["North America"] = na_countries
	target_eu.close()

	target_sa = open("../data/txt/sa.txt", "r")
	sa_countries = []
	for line in target_sa:
		country = {}
		country["name"] = line.rstrip('\n')
		sa_countries.append(country)
	countries["South America"] = sa_countries
	target_sa.close()

	target_asia = open("../data/txt/asia.txt", "r")
	asia_countries = []
	for line in target_asia:
		country = {}
		country["name"] = line.rstrip('\n')
		asia_countries.append(country)
	countries["Asia"] = asia_countries
	target_asia.close()

	target_africa = open("../data/txt/africa.txt", "r")
	africa_countries = []
	for line in target_africa:
		country = {}
		country["name"] = line.rstrip('\n')
		africa_countries.append(country)
	countries["Africa"] = africa_countries
	target_africa.close()

	target_oceania = open("../data/txt/oceania.txt", "r")
	oceania_countries = []
	for line in target_oceania:
		country = {}
		country["name"] = line.rstrip('\n')
		oceania_countries.append(country)
	countries["Oceania"] = oceania_countries
	target_oceania.close()

	for continent, values in countries.items():
		print(continent)
		print(values)

def to_json():
	json_data = json.dumps(countries)

	target = open("../data/json/countries.json", "w")

	target.write(json_data)

	target.close()

if __name__ == '__main__':
	fill_country_lists()
	to_json()



