# Recommendation Service

# API 

## Requirements <br>
<code> pip install uvicorn </code>
<code> pip install -r requirements.txt </code>

## Lancer le serveur de l'API de recommendation
1- Ouvrir le dossier fastapi_project/fastapi_project <br>
2- Ouvrir le terminal dans ce dossier <br>
3- Taper la commande : <code> uvicorn main:app --reload </code> <br>
4- Dans le navigateur acceder a la documentation de l'API avec le lien suivant: 127.0.0.1:8000/docs (NB: etre connecte a internet)


# Service de recommandation 


1- telecharger toutes les dépendances présentes sur le <i>pom.xml</i>
2- Pour la fonction <i>printRecommendTrip</i> , aller sur <i>RecommenderApplication.java</i> précisement sur la fonction <i> run</i> <br> décommenter ce qui est en commentaire et commenter ce qui n'est pas et lancer le projet
<ul>
  <li>La réponse normale doit être {"result": [liste des identifiants des tips]}</li>
  <li> tout autre résultats est une erreur</li>
</ul>
3- Pour la fonction <i>sortSearch</i> , lancer le projet
<ul>
  <li> 
    La réponse normale est du style
    <span>[com.letsgo.recommender_service.models.Trip@3e0855b0, com.letsgo.recommender_service.models.Trip@484b5a21, com.letsgo.recommender_service.models.Trip@16e5344c, com.letsgo.recommender_service.models.Trip@3ba5c4dd, com.letsgo.recommender_service.models.Trip@47fca3cc, com.letsgo.recommender_service.models.Trip@55746340, com.letsgo.recommender_service.models.Trip@6a0c7af6]</span>
  </li>
  <li> Tout autre réponse que cette dernière est considérer comme erreur</li>
</ul>


