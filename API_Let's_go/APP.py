
#pip install fastapi uvicorn

# 1. Library imports
import uvicorn ##ASGI
from fastapi import FastAPI
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

# Chargement des données

user_df = pd.read_csv("users.csv")
trajet_df=pd.read_csv("trajet.csv")
plan_trip_df= pd.read_csv("plan_trip.csv")
all_trip_df = pd.merge(plan_trip_df, trajet_df, on='trajetId')

"""cv = CountVectorizer(max_features=5000,stop_words='english')
vector = cv.fit_transform(user_df['keywords']).toarray()
similarity = cosine_similarity(vector)

def recommend(userId,dest_fav):# pour trouver les user simillaire
    #index = user_df[user_df['userId'] == movie].index[0]
    dest =[]
    dest.append(dest_fav)
    distances = sorted(list(enumerate(similarity[userId])),reverse=True,key = lambda x: x[1])
    for i in distances[0:30]:
        dest.append(user_df.iloc[i[0]].dest_fav)
    return dest

def recommend1(locality,dest_fav):# pour le cold start Traveler 
    userdf = user_df[user_df['u_type'] == 'Traveler']
    index = userdf[userdf['locality'] == locality].index[0]
    dest =[]
    dest.append(dest_fav)
    distances = sorted(list(enumerate(similarity[index])),reverse=True,key = lambda x: x[1])
    for i in distances[0:100]:
        dest.append(user_df.iloc[i[0]].dest_fav)
    return dest

def recommend2(locality,dest_fav):# pour le cold start planner  
    userdf = user_df[user_df['u_type'] == 'Planner']
    index = userdf[userdf['locality'] == locality].index[0] # locality vu comme point de depart trajet Favori...
    dest =[]
    dest.append(dest_fav)
    distances = sorted(list(enumerate(similarity[index])),reverse=True,key = lambda x: x[1])
    for i in distances[0:100]:
        dest.append(user_df.iloc[i[0]].dest_fav)
    return dest
app = FastAPI()
# Pour les nouveaux voyageurs
@app.get('/get_content_based_recommendations_for_New_Traveler')
def get_content_based_recommendations_for_New_Traveler(locality, dest_fav):
    # Filtrage des trajets en fonction de la localité et la destination favorite du voyageur
    filtered_trajets = trajet_df[(trajet_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend1(locality,dest_fav))]
    user_trips = filtered_trajets.sort_values(['price'], ascending=True)
    
    # Retourner les 10 trajets les plus pertinents
    return user_trips.head(10)

@app.get('/get_content_based_recommendations_for_Traveler')
def get_content_based_recommendations_for_Traveler(userId,locality, dest_fav):
    # Filtrage des trajets en fonction de la localité et la destination favorite du voyageur
    filtered_trajets = trajet_df[(trajet_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend(userId,dest_fav))]
    user_trips = filtered_trajets.sort_values(['price'], ascending=True)
    
    # Retourner les 10 trajets les plus pertinents
    return {user_trips.head(10)}
@app.get('/get_content_based_recommendations_for_New_planner')
def get_content_based_recommendations_for_New_planner(locality, dest_fav):
    # Filtrage des trajets reservé en fonction de la localité et la destination favorite du planner
    filtered_trajets = all_trip_df[(all_trip_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend2(locality,dest_fav))]
    trips = filtered_trajets.sort_values(['price'], ascending=False)
    
    
    # Retourner les 10 trajets les plus pertinents
    return { "Travertrips": trips["travelerId"][:10]}
@app.get('/get_content_based_recommendations_for_planner')
def get_content_based_recommendations_for_planner(userId,locality, dest_fav): # prend en entrée le userID sont trajet preferé et retourne les 10 travelers les plus pertinents
    # Filtrage des trajets reservé en fonction de la localité et la destination favorite du planner

    filtered_trajets = all_trip_df[(all_trip_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend(userId,dest_fav))]
    trips = filtered_trajets.sort_values(['price'], ascending=False)
    
    
    # Retourner les 10 travelers les plus pertinant
    return { "Traverler": trips["travelerId"][:10]}
    




# 2. Create the app object


# 3. Index route, opens automatically on http://127.0.0.1:8000
@app.get('/')
def index():
    return {'message': 'Hello, World §§§'}

# 4. Route with a single parameter, returns the parameter within a message
#    Located at: http://127.0.0.1:8000/AnyNameHere
@app.get('/Welcome')
def get_name(name: str):
    return {'Welcome To Theoricien  API ': f'{name}'}



# 5. Run the API with uvicorn
#    Will run on http://127.0.0.1:8000
if __name__ == '__main__':
    uvicorn.run(app, host='127.0.0.1', port=8005)
#uvicorn main:app --reload
"""

# Chargement des données
#user_df = pd.read_csv("C:\Users\fofou\Desktop\fastapi_project\fastapi_project\donnees\users.csv")
#trajet_df=pd.read_csv("./donnees/trajet.csv")
plan_trip_df= pd.read_csv("plan_trip.csv")
#all_trip_df = pd.merge(plan_trip_df, trajet_df, on='trajetId')


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
    for i in distances[0:100]:
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
    for i in distances[0:300]:
        dest.append(user_df.iloc[i[0]].dest_fav)
    return dest
def recommend4(locality):
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

    distances = sorted(list(enumerate(similarity[index])),reverse=True,key = lambda x: x[1])
    for i in distances[0:200]:
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
def recommend3(locality):
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

    distances = sorted(list(enumerate(similarity[index])),reverse=True,key = lambda x: x[1])
    for i in distances[0:100]:
        dest.append(user_df.iloc[i[0]].dest_fav)
    return dest

#definition des routes

app = FastAPI()

@app.get("/")
async def root():
 return {"greeting":"Hello Let's GO Recommender System"}


@app.get("/RecommendationsNewTraveler_0/{locality}")
async def get_content_based_recommendations_for_New_Traveler_0(locality: str):
    """Pour les nouveaux voyageurs premier contact avec l'application

    Args:
        locality (_str_)


    Returns:
         _list_: _liste des id des trajets_
    """
    # Filtrage des trajets en fonction de la localité et la destination favorite du voyageur
    filtered_trajets = trajet_df[(trajet_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend4(locality))]
    user_trips = filtered_trajets.sort_values(['price'], ascending=True)

    # Retourner les 10 trajets les plus pertinents
    result = user_trips['trajetId'].tolist()[:10]
    return {"result": result}


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
        _list: _liste des id des travelers_
    """
    # Filtrage des trajets reservé en fonction de la localité et la destination favorite du planner
    filtered_trajets = all_trip_df[(all_trip_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend2(locality,dest_fav))]
    trips = filtered_trajets.sort_values(['price'], ascending=False)
    
    result = trips["travelerId"].tolist()[:10]
   
    return {"result": result}


@app.get("/RecommendationsNewPlanner_0/{locality}")
async def get_content_based_recommendations_for_New_planner_0(locality: str):
    """Pour les nouveaux planners first contact with application

    Args:
        locality (_str_)


    Returns:
        _list: _liste des id des travelers_
    """
    # Filtrage des trajets reservé en fonction de la localité et la destination favorite du planner
    filtered_trajets = all_trip_df[(all_trip_df['start_place'] == locality)]
    filtered_trajets = filtered_trajets[filtered_trajets['end_place'].isin(recommend3(locality))]
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

if __name__ == '__main__':
    uvicorn.run(app, host='127.0.0.1', port=8000)
#uvicorn main:app --reload
