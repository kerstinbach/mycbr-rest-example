import random as r
import json

rating = [1,2,3,4,5]
period = range(2010,2017)
continents = ["Asia", "Europe", "South America", "North America", "Oceania", "Africa"]
institutes, languages, universities, subjects = [], [], [], []

countries_json = open("data/json/countries.json", 'r')
countries_data = json.load(countries_json)


def generate_csv():
	target = open("../../src/main/resources/test_cases.csv", "w")

	target.write("Institute;Continent;Country;University;StudyPeriod;Language;AcademicQuality;SocialQuality;Subjects" + '\n')

	for i in range(1, 500):
		continent = r.choice(continents)
		country = r.choice(countries_data[continent])["name"]
		university = r.choice(universities)
		language = r.choice(languages)
		academic_rating = r.choice(rating)
		social_rating = r.choice(rating)
		institute = r.choice(institutes)
		subjects_chosen = r.choice(subjects) + '!' + r.choice(subjects) + '!' + r.choice(subjects) + '!' + r.choice(subjects)

		target.write(institute + ';' + continent + ';' + country + ';' + 
			university + ';' + str(r.choice(period)) + ';' + 
			language + ';' + str(r.choice(rating)) + ';' + str(r.choice(rating)) + ';' + subjects_chosen +'\n')

	target.close()


def get_data():
	file1 = open("attributes/institutes.txt", "r")
	for line in file1:
		institutes.append(line.rstrip('\n'))
	file1.close()

	file2 = open("attributes/universities.txt", "r")
	for line in file2:
		universities.append(line.rstrip('\n'))
	file2.close()

	file3 = open("attributes/languages.txt", "r")
	for line in file3:
		languages.append(line.rstrip('\n'))
	file3.close()

	file4 = open("attributes/countries.txt", "r")
	for line in file4:
		countries.append(line.rstrip('\n'))
	file4.close()

	file5 = open("attributes/subjects.txt", "r")
	for line in file5:
		subjects.append(line.rstrip('\n'))
	file5.close()


if __name__ == "__main__":
	get_data()
	generate_csv()

