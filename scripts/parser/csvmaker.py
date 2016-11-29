import json


def make_csv(data):
	output = open('../../src/main/resources/cases.csv', 'w')

	output.write("Institute;Continent;Country;University;StudyPeriod;Language;AcademicQuality;SocialQuality;Subjects" + '\n')

	for case in data:
		courses = ""
		final_course = len(case['courses'])
		counter = 1
		for course in case['courses']:
			courses += course
			if counter == final_course:
				print("he")
			else:
				courses += '!'
				counter += 1
		

		output.write(
			case['institute'] + ';' +
			str(case['continent']) + ';' +
			case['country'] + ';' +
			case['university'] + ';' +
			str(case['study_period']) + ';' +
			case['language'] + ';' +
			str(case['academic_quality']) + ';' +
			str(case['social_quality']) + ';' +
			courses + '\n'
			)

	output.close()
	


if __name__ == "__main__":
	with open('output/cases.json', 'r') as data_file:
		data = json.load(data_file)
	make_csv(data)






