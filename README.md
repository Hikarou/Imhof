# Imhof
Le but du projet est de dessiner des cartes topographiques au 1:50'000 dont le style s'inspire de celui des cartes suisses. Ce projet est nommé Imhof en l'honneur d'Eduard Imhof (1895–1986), cartographe suisse célèbre — entre autres — pour ses magnifiques cartes en relief.

## Utilisation
Pour lancer le projet, il faut une carte osm au format `.osm.gz`, une description de gradient de la même zone au format `.hgt` les longitudes et latitudes des coins bas-gauche et haut-droit.

## Résultats attendus
Les résultats attendus sont les suivants :

### Lausanne
`data/map/lausanne.osm.gz data/hgt/N46E006.hgt 6.5594 46.5032 6.6508 46.5459 300 lausanne.png`

![Lausanne](./data/pictures/lausanne.png)

### Interlaken
`data/map/interlaken.osm.gz data/hgt/N46E007.hgt 7.8122 46.6645 7.9049 46.7061 300 interlaken.png`

![Interlaken](./data/pictures/interlaken.png)

### Berne
`data/map/berne.osm.gz data/hgt/N46E007.hgt 7.3912 46.9322 7.4841 46.9742 300 berne.png`

![Berne](./data/pictures/berne.png)


Toutes les étapes sont décrites dans la catégorie "Projet" sur cette [page](https://cs108.epfl.ch/archive/15/archive.html#unnumbered-3) du cours "Pratique de la programmation orientée-objet" de [Michel Schinz](https://people.epfl.ch/michel.schinz) à l'EPFL.