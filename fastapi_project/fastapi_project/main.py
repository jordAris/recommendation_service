# main.py

#librairie pour l'app
from fastapi import FastAPI

#librairies pour la recommandation
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import CountVectorizer


# Chargement des données
user_df = pd.read_csv("donnees/users.csv")
trajet_df=pd.read_csv("donnees/trajet.csv")
plan_trip_df= pd.read_csv("donnees/plan_trip.csv")
all_trip_df = pd.merge(plan_trip_df, trajet_df, on='trajetId')


#traitement sur les donnees 
cv = CountVectorizer(max_features=5000,stop_words='english')
vector = cv.fit_transform(user_df['keywords']).toarray()
similarity = cosine_similarity(vector)


def recommend(userId,dest_fav):
    """_pour trouver les user simillaire_

    Args:
        userId (_int_)
        dest_fav (_list_)

    Returns:
        dest _list_: _liste de destinations_
    """
    #index = user_df[user_df['userId'] == movie].index[0]
    dest =[]
    dest.append(dest_fav)
    distances = sorted(list(enumerate(similarity[userId])),reverse=True,key = lambda x: x[1])
    for i in distances[0:30]:
        dest.append(user_df.iloc[i[0]].dest_fav)
    return dest



def recommend1(locality,dest_fav):
    """_pour le cold start Traveler_

    Args:
        locality (_char_)
        dest_fav (_char_)

    Returns:
        _list_: _liste de destinations_
    """
    userdf = user_df[user_df['u_type'] == 'Traveler']
    index = userdf[userdf['locality'] == locality].index[0]
    dest =[]
    dest.append(dest_fav)
    distances = sorted(list(enumerate(similarity[index])),reverse=True,key = lambda x: x[1])
    for i in distances[0:100]:
        dest.append(user_df.iloc[i[0]].dest_fav)
    return dest


def recommend2(locality,dest_fav):
    """_pour le cold start planner_

    Args:
        locality (_type_)
        dest_fav (_type_)

    Returns:
        _list_: _liste de destinations_
    """
    userdf = user_df[user_df['u_type'] == 'Planner']
    index = userdf[userdf['locality'] == locality].index[0] # locality vu comme point de depart trajet Favori...
    dest =[]
    dest.append(dest_fav)
    distances = sorted(list(enumerate(similarity[index])),reverse=True,key = lambda x: x[1])
    for i in distances[0:100]:
        dest.append(user_df.iloc[i[0]].dest_fav)
    return dest

#definition des routes

app = FastAPI()

@app.get("/")
async def root():
 return {"greeting":"Hello Let's GO Recommender System"}


@app.get("/RecommendationsNewTraveler/{locality}/{dest_fav}")
async def get_content_based_recommendations_for_New_Traveler(locality: str, dest_fav: str):
    """Pour les nouveaux voyageurs

    Args:
        locality (_str_)
        dest_fav (_str_)

    Returns:
         _list_: _liste des id des trajets_
    """
    # Filtrage des trajets en fonction de la localité et la destination favorite du voyageur
    filtered_trajets = trajet_df[(trajet_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend1(locality,dest_fav))]
    user_trips = filtered_trajets.sort_values(['price'], ascending=True)
    
    # Retourner les 10 trajets les plus pertinents
    result = user_trips['trajetId'].tolist()[:10]
    return {"result": result}

@app.get("/RecommendationsTraveler/{userId}/{locality}/{dest_fav}")
async def get_content_based_recommendations_for_Traveler(userId: int,locality: str, dest_fav: str):
    """Pour les voyageurs deja dans la platforme

    Args:
        userId (_int_)
        locality (_str_)
        dest_fav (_str_)

    Returns:
        _list_: _liste des id des trajets_
    """
    # Filtrage des trajets en fonction de la localité et la destination favorite du voyageur
    filtered_trajets = trajet_df[(trajet_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend(userId,dest_fav))]
    user_trips = filtered_trajets.sort_values(['price'], ascending=True)
    
    # Retourner les 10 trajets les plus pertinents
    result = user_trips['trajetId'].tolist()[:10]
    return {"result": result}

@app.get("/RecommendationsNewPlanner/{locality}/{dest_fav}")
async def get_content_based_recommendations_for_New_planner(locality: str, dest_fav: str):
    """Pour les nouveaux planners

    Args: 
        locality (_str_)
        dest_fav (_str_)

    Returns:
        _list: _liste des id des trajets_
    """
    # Filtrage des trajets reservé en fonction de la localité et la destination favorite du planner
    filtered_trajets = all_trip_df[(all_trip_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend2(locality,dest_fav))]
    trips = filtered_trajets.sort_values(['price'], ascending=False)
    
    result = trips["travelerId"].tolist()[:10]
   
    return {"result": result}

@app.get("/RecommendationsPlanner/{userId}/{locality}/{dest_fav}")
async def get_content_based_recommendations_for_planner(userId: int,locality: str, dest_fav: str): 
    """prend en entrée le userID sont trajet preferé et retourne les 10 travelers les plus pertinents

    Args:
        userId (_int_)
        locality (_str_)
        dest_fav (_str_)

    Returns:
        _list_: _liste des travelers _
    """
    # Filtrage des trajets reservé en fonction de la localité et la destination favorite du planner

    filtered_trajets = all_trip_df[(all_trip_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend(userId,dest_fav))]
    trips = filtered_trajets.sort_values(['price'], ascending=False)
    
    # Retourner les 10 travelers les plus pertinant
    result = trips["travelerId"].tolist()[:10]
    return {"result": result}

