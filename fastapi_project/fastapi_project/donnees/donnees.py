#librairies utilisees
import pandas as pd
import random
import csv
import time
from datetime import datetime, timedelta

#Listes qui seront communes a plusieurs attributs
cameroon_places = [#taille 50
    "Bali",
    "Bamenda",
    "Banga Bakundu",
    "Bertoua",
    "Bimbia",
    "Buea",
    "Bafia",
    "Bafang",
    "Bafoussam",
    "Bali Nyonga",
    "Bandjoun",
    "Batibo",
    "Belabo",
    "Belo",
    "Bertoua",
    "Beti",
    "Bogo",
    "Bonaberi",
    "Bonabéri",
    "Bonakouamouang",
    "Bonamoussadi",
    "Bonanjo",
    "Boutours",
    "Buea",
    "Campo",
    "Dibombari",
    "Douala",
    "Edéa",
    "Essos",
    "Figuif",
    "Fontem",
    "Garoua",
    "Gazawa",
    "Guider",
    "Kribi",
    "Kumba",
    "Limbe",
    "Loum",
    "Maroua",
    "Mbalmayo",
    "Mbanga",
    "Mbandjock",
    "Mbouda",
    "Meiganga",
    "Melong",
    "Messa",
    "Mokolo",
    "Mora",
    "Mutengene",
    "Muyuka",
    "Nanga-Eboko",
    "Nkongsamba","Yaounde","Yagoua","Kousseri"
]

#fonctions utiles


def generer_date():
    # Générer une date au hasard entre 2000 et 2023
    timestamp = random.randint(94668480000, 167256960000)
    date = datetime.fromtimestamp(timestamp)

    return date

###Pour les Users dans un fichier **users.csv**
####Nous allons generer 2000 utilisateurs

#liste des users type
u_types = ['Traveler', 'Planner']

#la liste de keywords
KeyWords = [ #taille 22
    "Confort",
    "Musique",
    "Silence",
    "Climatisation",
    "Chauffeur expérimenté",
    "Ponctualité",
    "Espace de rangement",
    "Connectivité",
    "Conversation",
    "Vue panoramique",
    "Ambiance chaleureuse",
    "Boisson gratuite",
    "Animaux acceptés",
    "Sièges inclinables",
    "Éclairage tamisé",
    "Conduite souple",
    "Navigation GPS",
    "Assistance bagages",
    "Langue parlée",
    "Wi-Fi gratuit",
    "Sport", "Cuisine"]


with open('../donnees/users.csv', mode='w', newline='') as csv_file:
    writer = csv.writer(csv_file)

    writer.writerow(['userId','locality', 'keywords', 'u_type', 'dest_fav'])
   
    # Générer 2000 occurrences aléatoires
    for i in range(5001):
        userId = random.randint(1, 5000)

        num_keywords = random.randint(1, len(KeyWords))
        selected_keywords = random.sample(KeyWords, num_keywords)

        
        locality = random.choice(cameroon_places)

        u_type = random.choice(u_types)

        dest_fav = random.choice(cameroon_places)

        writer.writerow([userId, locality, ','.join(selected_keywords), u_type, dest_fav])


###Pour les Cars dans un fichier **vehicule.csv**
####Nous allons generer 400 Vehicules

users = pd.read_csv('../donnees/users.csv')
users_driver = users[users['u_type'] == 'Planner']

with open('../donnees/vehicules.csv', mode='w', newline='') as csv_file:
    writer = csv.writer(csv_file)

    writer.writerow(['vehiculeId','plannerId'])
    
    # Générer 400 occurrences aléatoires
    for i in range(1500):
        carId = random.randint(1, 1000)
        users_driver = users[users['u_type'] == 'Planner']
        
        l = users_driver['userId'].to_list()
        plannerId = random.choice(l)

        writer.writerow([carId, plannerId])


###Pour les trajets dans un fichier **trajet.csv**
####Nous allons generer 200 trajets qui seront chacun associe a chaque plan trip
#Trajet: trajetId, start_place, end_place , cost

with open('../donnees/trajet.csv', mode='w', newline='') as csv_file:
    writer = csv.writer(csv_file)

    writer.writerow(['trajetId', 'start_place', 'end_place', 'price'])
    
    # Générer 200 occurrences aléatoires
    for i in range(500):
        trajetId = random.randint(1, 1000)
        start_place = random.choice(cameroon_places)
        end_place = random.choice(cameroon_places)
        price = random.randrange(100, 10000, 200)
        if start_place == end_place:
          end_place = random.choice(cameroon_places)
        else:
          pass

        writer.writerow([trajetId, start_place, end_place, price])


###Pour les PlanTrip dans un fichier **plan_trip.csv**
####Nous allons generer 200 plan trip

#PlanTrip: PlanTripId, CarId, trajetId, plannerID, add_at, start_at, end_at

users = pd.read_csv('../donnees/users.csv')
trajets = pd.read_csv('../donnees/trajet.csv')
vehicule = pd.read_csv('../donnees/vehicules.csv')

users_driver = users[users['u_type'] == 'Planner']

users_traveler = users[users['u_type'] == 'Traveler']

with open('../donnees/plan_trip.csv', mode='w', newline='') as csv_file:
    writer = csv.writer(csv_file)

    writer.writerow(['plantripId','vehiculeId', 'trajetId', 'plannerId', 'travelerId', 'add_at', 'start_at','end_at'])
    
    # Générer 200 occurrences aléatoires
    for i in range(200):
        plantripId = random.randint(1, 1000)
        vehiculeId = random.choice(vehicule['vehiculeId'].to_list())
        trajetId = trajets['trajetId'].to_list()[i]
        plannerId = random.choice(users_driver['userId'].to_list())

        travelerId = random.choice(users_traveler['userId'].to_list())

        add_at = generer_date()
        start_at = add_at + timedelta(days=1)
        end_at = start_at + timedelta(random.randint(30, 2880))# aléatoire(30m,48h)
        writer.writerow([plantripId, vehiculeId, trajetId, plannerId, travelerId, add_at, start_at, end_at])
     # Générer 200 occurrences aléatoires
    for i in range(300):
        plantripId = random.randint(1001, 2000)
        vehiculeId = random.choice(vehicule['vehiculeId'].to_list())
        trajetId = trajets['trajetId'].to_list()[i]
        travelerId = random.choice(users_traveler['userId'].to_list())

        add_at = generer_date()
        start_at = add_at + timedelta(days=1)
        end_at = start_at + timedelta(minutes = 30)
        writer.writerow([plantripId, vehiculeId, trajetId, plannerId, travelerId, add_at, start_at, end_at])

plantrip = pd.read_csv('../donnees/plan_trip.csv')