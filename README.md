# Distributed chat room

Bienvenue sur le projet WS de mise en place d'un salon de discussion distribué. Afin que le repository soit bien utilisé, je me permettrais de donner quelques indications qui nous faciliteront le travail:

- Toujours faire précéder les descriptions de commit par son trigramme entre crochets, c'est-à-dire la première lettre du prénom et les deux premières lettres du nom, comme ça on saura plus facilement qui a fait quoi;
Exemple: [MSA] exemple de commit
- Rendre les commit le plus atomique possible: par là j'entends qu'un commit ne doit porter que sur une fonctionnalité à la fois, cela facilite les revert;

À noter que la structure du projet (maven) est répartie comme suit: un grand projet distributedChatRoom, au sein duquel se trouvent sous-projets client, loadBalancer et chatRoom. Vous n'aurez qu'à importer le grand projet distribuedChatRoom avec votre IDE favori pour que le tout soit présent.

Voilà c'est tout ! Pour ceux qui ne sont pas encore familiers avec git, je vous invite à consulter le tuto d'openclassrooms pour une prise en main rapide: https://openclassrooms.com/courses/gerez-vos-codes-source-avec-git
