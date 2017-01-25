import urllib.request as urllib
from html.parser import HTMLParser
import data.countryinfo as ci
import data.universityinfo as ui
import data.instituteInfo as ii
from fuzzywuzzy import fuzz
from fuzzywuzzy import process
import json
import glob
from titlecase import titlecase
import re
import time
import math

data_list = []
star_list = []
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

        elif element == "Undervisningsspråk:":
            next_element = data_list[i+2]
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
        data_object["residential_quality"] = get_rating("residential")
        data_object["reception_quality"] = get_rating("reception")
        

    return data_object


def format_study_period(s):
    for c in s:
        if c == ';' or c == '!':
            s = s.replace(c, '')
    if len(s.strip()) > 1:
        return int(re.match(r'.*([1-3][0-9]{3})', s).group(1))
    return "Ukjent"


def format_language(s):
    s = s.strip()

    for c in s:
        if c == ';' or c == '!':
            s = s.replace(c, '')
        elif c == ',' or c == '/':
            s = s.replace(c, ' ')

    s = s.split()

    for word in s:
        if word == 'og':
            index = s.index(word)
            del s[index]

    s = [element.title() for element in s]

    s = '!'.join(s)

    return s


def format_country(s):
    s = s.strip()
    for c in s:
        if c == ';' or c == '!' or c == '\n':
            s = s.replace(c, '')
    if s.isupper():
        return s
    else:
        return titlecase(s)
    

def get_continent(s):
    for c in s:
        if c == ';' or c == '!':
            s = s.replace(c, '')
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


"""
Looks in our list of pre-formated insitutes, and returns the most similar one
"""
def format_institute(s):
    for c in s:
        if c == ';' or c == '!':
            s = s.replace(c, '')
    if len(s) < 5:
        for institute in ii.institutes:
            if institute['acronym'].lower() == s.lower():
                return str(institute['faculty']) + '-' + str(institute['acronym']) + ' - ' + str(institute['name'])

   
    best_match = {'name': "", 'score': 0}
    for institute in ii.institutes:
        if fuzz.ratio(s, str(institute['name'])) > best_match['score']:
            best_match['name'] = str(institute['faculty']) + '-' + str(institute['acronym']) + ' - ' + str(institute['name'])
            best_match['score'] = fuzz.ratio(s, institute['name'])
    return best_match['name']


def format_university(s):
    global unique_unis
    s = s.strip()
    s = s.title()

    for c in s:
        if c == ';' or c == '!':
            s = s.replace(c, '')

    # Strange edge-case
    if fuzz.ratio(s, "Universidad Politcnica de Madrid") > 90:
        return "Universidad Politécnica de Madrid"

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

    # If the university is allready present in another case, or it is very similar to an allready added
    # university, that university is added instead, to avoid having several similar ways to write it
    for uni in unique_unis:
        if (s == uni) or (fuzz.ratio(s, uni.strip()) > 85):
            return uni            

    # If the university is in all caps lock, it is asumed that it is an acronym. This acronym is looked up in the 
    # acronym - university name dictionary, and returns its name. If the acronym isn't present, the string is simply returned
    if len(s) < 6:
        for u in ui.universities:
            if u['acronym'].lower() == s.lower():
                for uni in unique_unis:
                    if (fuzz.ratio(u['name'].title(), uni.strip()) > 85):
                        return uni
                unique_unis.append(u['name'].title())
                return u['name'].title()
        unique_unis.append(s.upper())
        return s.upper()
    else:
        unique_unis.append(s)
        return s.title()

def format_course(s):
    # Checking if the course contains a '!' or ';', which will break the formating
    for c in s:
        if c == '!' or c == ';' or c =='\n':
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
    star_counter = 0

    if s == "academic":
        # Akademisk kvlaitet
        star_counter += calculate_stars(181, 186)

        # Spesialkompetanse ute
        star_counter += (calculate_stars(186, 191)) * 0.6

        # Kvalitet på faglige tilbud
        star_counter += calculate_stars(161, 166)

        return math.ceil((star_counter / 3) * 2)

    elif s == "social":
        # Sosialt tilbud generelt
        star_counter += calculate_stars(136, 141)

        # Fritidstilbud generelt
        star_counter += calculate_stars(141, 146)

        # Kjæreste/venner i utlandet
        star_counter += calculate_stars(191, 196)

        # Sosial integrasjon med...
        # Studenter fra universitetet
        star_counter += calculate_stars(146, 151)

        # Andre utenlandske studenter
        star_counter += calculate_stars(151, 156)

        # Norske studenter
        star_counter += calculate_stars(156, 161)


        return math.ceil((star_counter / 6) * 2)

    elif s == "reception":
        #Mottak generelt
        star_counter += calculate_stars(111, 116)

        #Administrativ støtte
        star_counter += calculate_stars(116, 121)

        return math.ceil((star_counter / 2) * 2)

    elif s == "residential":
        # Hybelformidling
        star_counter += calculate_stars(121, 126)

        # Hybelstandard
        star_counter += calculate_stars(126, 131)

        return math.ceil((star_counter / 2) * 2)

    else:
        return None

def calculate_stars(start, end):
    temp = []
    counter = 0

    for i in range(start, end):
        temp.append(star_list[i])

    for row in temp:
        if row[0][1] == "fa fa-star":
            counter += 1

    return counter





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
        print("Starting: " + str(file))
        case = run(file)
        if not case["courses"] or len(case["institute"]) == 0 or len(str(case["study_period"])) > 4 or len(str(case["university"])) > 60:
            continue
        if stopper >= 100000:
            return
        cases.append(case) 
        counter += 1
        stopper += 1
        print("Finished: " + str(file) + " (" + str(counter) + '/' + "10857)")


def make_csv():

    output = open('../../src/main/resources/cases.csv', 'w')
    output.write("Institute;Continent;Country;University;StudyPeriod;Language;AcademicQuality;SocialQuality;ResidentialQuality;ReceptionQuality;Subjects" + '\n')

    for case in cases:
        courses = ""
        final_course = len(case['courses'])
        counter = 1
        for course in case['courses']:
            courses += str(course)
            if counter == final_course:
                break
            else:
                courses += '!'
                counter += 1
        

        output.write(
            str(case['institute']) + ';' +
            str(case['continent']) + ';' +
            str(case['country']) + ';' +
            str(case['university']) + ';' +
            str(case['study_period']) + ';' +
            str(case['language']) + ';' +
            str(case['academic_quality']) + ';' +
            str(case['social_quality']) + ';' +
            str(case['residential_quality']) + ';' +
            str(case['reception_quality']) + ';' +
            str(courses) + '\n'
            )

    output.close()

    print("Finished CSV file")



if __name__ == "__main__":
    start()
    print_cases()
    make_csv()

    