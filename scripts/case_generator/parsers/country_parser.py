import json
from pprint import pprint
import random as r

countries = {}

europe_countries = []
na_countries = []
sa_countries = []
asia_countries = []
oceania_countries = []
africa_countries = []

europe_languages = []
na_languages = []
sa_languages = []
asia_languages = []
oceania_languages = []
africa_languages = []


def fill_country_lists():
	target_eu = open("../data/txt/europe.txt", "r")
	for line in target_eu:
		europe_countries.append(line.rstrip('\n'))
	countries["Europe"] = europe_countries
	countries["Europe"]["Languages"] = r.choice("English", "Deutsch", "French", "Spanish")
	target_eu.close()

	target_na = open("../data/txt/na.txt", "r")
	for line in target_na:
		na_countries.append(line.rstrip('\n'))
	countries["North America"] = na_countries
	countries["Languages"] = r.choice(["English", "Canadian"])
	target_eu.close()

	target_sa = open("../data/txt/sa.txt", "r")
	for line in target_sa:
		sa_countries.append(line.rstrip('\n'))
	countries["South America"] = sa_countries
	countries["Languages"] = r.choice("[English", "Spanish", "Portuguese]")
	target_sa.close()

	target_asia = open("../data/txt/asia.txt", "r")
	for line in target_asia:
		asia_countries.append(line.rstrip('\n'))
	countries["Asia"] = asia_countries
	countries["Languages"] = r.choice("[English", "Chinese", "Japanese", "Indian]")
	target_asia.close()

	target_africa = open("../data/txt/africa.txt", "r")
	for line in target_africa:
		africa_countries.append(line.rstrip('\n'))
	countries["Africa"] = africa_countries
	countries["Languages"] = r.choice(["English"])
	target_africa.close()

	target_oceania = open("../data/txt/oceania.txt", "r")
	for line in target_oceania:
		oceania_countries.append(line.rstrip('\n'))
	countries["Oceania"] = oceania_countries
	countries["Languages"] = r.choice(["English"])
	target_oceania.close()

	print(countries)

def to_json():
	json_data = json.dumps(countries)

	target = open("../data/json/countries.json", "w")

	target.write(json_data)

	target.close()

if __name__ == '__main__':
	fill_country_lists()
	to_json()



