import urllib.request as urllib
from html.parser import HTMLParser
import data.countryinfo as ci
import data.universityinfo as ui
from fuzzywuzzy import fuzz
from fuzzywuzzy import process
import json
import glob
from titlecase import titlecase
import re
import time

data_list = []
star_list = []
institutes = []
unique_unis = []

cases = []


class Parser(HTMLParser):

    def __init__(self):
        HTMLParser.__init__(self)

    def destroy(self):
        del self

    def handle_data(self, data):
        global data_list
        data_list.append(data)

    def handle_starttag(self, tag, attrs):
        if attrs == [('class', 'fa fa-star-o'), ('aria-hidden', 'true')] or attrs == [('class', 'fa fa-star'), ('aria-hidden', 'true')] or attrs == [('class', 'fa fa-star')] or attrs == [('class', 'fa fa-star-o')]:
            star_list.append(attrs)


def get_html():
    url = "Internasjonal Seksjon.html"
    result = urllib.urlopen(url)
    return result.read().decode("utf-8").replace('\r\n', ' ')


def parse_html():
    data_object = {}
    data_object["courses"] = []
    for i in range(0, len(data_list)):
        element = data_list[i]
        if element == "Land:":
            next_element = data_list[i+1]
            data_object['country'] = format_country(next_element)
            data_object['continent'] = get_continent(next_element)

        elif element == "Vertsinstitusjon:":
            next_element = data_list[i+1]
            data_object['university'] = format_university(next_element)

        elif element == "Institutt hjemme:":
            next_element = data_list[i+1]
            data_object['institute'] = format_institute(next_element)

        elif element == "Utvekslingsperiode:":
            next_element = data_list[i+1]
            data_object['study_period'] = format_study_period(next_element)

        elif element == "Studietype:":
            next_element = data_list[i+1]
            data_object['language'] = format_language(next_element)

        elif element == "Fag 1:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 2:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 3:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 4:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 5:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 6:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 7:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 8:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        data_object["academic_quality"] = get_rating("academic")
        data_object["social_quality"] = get_rating("social")

    return data_object


def format_study_period(s):
    return int(re.match(r'.*([1-3][0-9]{3})', s).group(1))


def format_language(s):
    for word in s.split():
        if word.lower() == "english" or word.lower() == "engelsk":
            return "Engelsk"
        elif word.lower() == "vertsinstitusjonens":
            return "Fag på vertsinstitusjonens undervisningsspråk"

    return "Ukjent"


def format_country(s):
    if s.isupper():
        return s
    else:
        return titlecase(s)
    

def get_continent(s):
    if s.lower() == "usa":
        return "North America"
    elif s.lower() == "sør afrika":
        return "Africa"
    elif s.lower() == "korea":
        return "Asia"
    else :
        for c in ci.countries:
            if c['name'].lower() == s.lower():
                if c['continent'] == None:
                    return "Ukjent"
                return c['continent'].title()
        return "Ukjent"


def get_institutes():
    file = open("data/institutes.txt", 'r')
    for line in file.readlines():
        institutes.append(line.strip())
    file.close()

"""
Looks in our list of pre-formated insitutes, and returns the most similar one
"""
def format_institute(s):
    best_match = {'name': "", 'score': 0}
    for institute in institutes:
        if fuzz.ratio(s, institute) > best_match['score']:
            best_match['name'] = institute
            best_match['score'] = fuzz.ratio(s, institute)
    return best_match['name']


def format_university(s):
    global unique_unis
    s = s.strip()

    # Removing any parts of the university string which is inside quotation marks or parantheses
    for char in s:
        if char == '(':
            s = s.split('(')
            del s[-1]
            s = ' '.join(s)
        elif char == '"':
            s = s.split('"')
            del s[-1]
            s = ' '.join(s)

    # If the university is allready preent in another case, or it is very similar to an allready added
    # university, that university is added instead, to avoid having several similar ways to write it
    for uni in unique_unis:
        if (s == uni) or (fuzz.ratio(s, uni.strip()) > 85):
            return uni            

    # If the university is in all caps lock, it is asumed that it is an acronym. This acronym is looked up in the 
    # acronym - university name dictionary, and returns its name. If the acronym isn't present, the string is simply returned
    if s.isupper():
        for u in ui.universities:
            if u['acronym'].lower() == s.lower():
                unique_unis.append(titlecase(u['name']))
                return titlecase(u['name'])
        unique_unis.append(s)
        return s
    else:
        unique_unis.append(s)
        return titlecase(s)

def format_course(s):
    # Checking if the course contains a '!' or ';', which will break the formating
    for c in s:
        if c == '!' or c == ';':
            s = s.replace(c, '')

    words = s.split()

    first_part = words[0]

    if len(words) > 1:
        second_part = words[1]

        for char in second_part:
            if char.isdigit():
                words.pop(0)
                words.pop(0)
                return first_part + second_part + ' ' + " ".join(str(x) for x in words)

    for char in first_part:
        if char.isdigit():
            return s

    return "XXXX" + " " + s


def get_rating(s):
    temp = []
    star_counter = 0
    if s == "academic":
        for i in range(181, 186):
            temp.append(star_list[i])

        for row in temp:
            if row[0][1] == "fa fa-star":
                star_counter += 1

        return star_counter

    elif s == "social":
        for i in range(136, 141):
            temp.append(star_list[i])

        for row in temp:
            if row[0][1] == "fa fa-star":
                star_counter += 1

        return star_counter

    else:
        return None

def run(s):
    global data_list
    global star_list
    html = open(s, 'r')
    P = Parser()
    P.feed(html.read())
    html.close()
    obj = parse_html()
    P.destroy()
    P.close()
    data_list = []
    star_list = []
    return obj


def print_cases():
    for case in cases:
        for k, v in case.items():
            print(k + ':', v)
        print('-'*100)


def start():
    global unique_unis

    data_list = []
    star_list = []
    file_list = glob.glob("retrieved_html_files/*.html")
    counter = 0
    stopper = 0
    for file in file_list:
        case = run(file)
        if not case["courses"]:
            continue
        if stopper >= 10000:
            return
        cases.append(case) 
        counter += 1
        stopper += 1
        print("Finished: " + str(file) + " (" + str(counter) + '/' + "3644)")


def make_csv():

    output = open('../../src/main/resources/cases.csv', 'w')
    output.write("Institute;Continent;Country;University;StudyPeriod;Language;AcademicQuality;SocialQuality;Subjects" + '\n')

    for case in cases:
        courses = ""
        final_course = len(case['courses'])
        counter = 1
        for course in case['courses']:
            courses += course
            if counter == final_course:
                break
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

    print("Finished CSV file")



if __name__ == "__main__":
    get_institutes()
    start()
    #print_cases()
    make_csv()

    