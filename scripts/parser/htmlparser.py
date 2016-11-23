import urllib.request as urllib
from html.parser import HTMLParser
import countryinfo as ci
from fuzzywuzzy import fuzz
from fuzzywuzzy import process

data_list = []
star_list = []
data_object = {}
data_object["courses"] = []
institutes = []


class Parser(HTMLParser):

    def handle_data(self, data):
        data_list.append(data)

    def handle_starttag(self, tag, attrs):
        if attrs == [('class', 'fa fa-star-o'), ('aria-hidden', 'true')] or attrs == [('class', 'fa fa-star'), ('aria-hidden', 'true')]:
            star_list.append(attrs)


def get_html():
    url = "Internasjonal Seksjon.html"
    result = urllib.urlopen(url)
    return result.read().decode("utf-8").replace('\r\n', ' ')

def get_html_local():
    file = open("Internasjonal Seksjon.html", 'r').read()
    return file


def parse_html():
    for i in range(0, len(data_list)):
        element = data_list[i]
        if element == "Land:":
            next_element = data_list[i+1]
            data_object['country'] = next_element
            data_object['continent'] = get_continent(next_element)

        if element == "Vertsinstitusjon:":
            next_element = data_list[i+1]
            data_object['university'] = next_element

        if element == "Institutt hjemme:":
            next_element = data_list[i+1]
            data_object['institute'] = format_institute(next_element)

        if element == "Utvekslingsperiode:":
            next_element = data_list[i+1]
            data_object['study_period'] = format_study_period(next_element)

        if element == "Studietype:":
            next_element = data_list[i+1]
            data_object['language'] = format_language(next_element)

        if element == "Fag 1:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        if element == "Fag 2:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(next_element)

        if element == "Fag 3:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(next_element)

        if element == "Fag 4:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(next_element)

        if element == "Fag 5:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(next_element)

        if element == "Fag 6:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(next_element)

        if element == "Fag 7:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(next_element)

        if element == "Fag 8:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(next_element)

        data_object["academic_quality"] = get_rating("academic")
        data_object["social_quality"] = get_rating("social")


def format_study_period(s):
    formated_study_period = ""
    for char in s:
        if char.isdigit():
            formated_study_period += char
    formated_study_period = int(formated_study_period)
    return(formated_study_period)


def format_language(s):
    if s == "Fag på vertsinstitusjonens undervisningsspråk":
        return "Ukjent"
    else:
        return s.split()[-1].title()


def get_continent(s):
    if s.lower() == "usa":
        return "North America"
    else :
        for c in ci.countries:
            if c['name'].lower() == s.lower():
                return c['continent'].title()


def get_institutes():
    file = open("institutes.txt", 'r')
    for line in file.readlines():
        institutes.append(line.strip())
    file.close()


def format_institute(s):
    best_match = {'name': "", 'score': 0}
    for institute in institutes:
        if fuzz.ratio(s, institute) > best_match['score']:
            best_match['name'] = institute
            best_match['score'] = fuzz.ratio(s, institute)
    return best_match['name']


def format_course(s):
    return s


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



if __name__ == "__main__":
    get_institutes()
    format_institute("Institutt for Fysik")
    html = get_html_local()
    P = Parser()
    P.feed(html)
    parse_html()
    print(data_object)
    