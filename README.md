# ISIS2304-IT2

Mateo Devia - 201630956
Felipe Velásquez - 201632422

Pasos para instalar la aplicacion.

1. Tener los plugins y servidores necesarios- Seguir el tutorial de arquitectura rest de sicua plus
2. Importar el proyecto en eclipse
3. Hacer click derecho en el proyecto -> Run as -> Run on Serever y seleccionar el servidor apropiado (Revisar tutorial de arquitectura rest). Una vez desplegada la aplicacion, podra interactual con ella a tra vez de lainterfaz grafica, o postman.

---------Para poblar la base de datos-----------------
1. Correr el script SCRIPTS_CON_TODO disponible en la documentacion del proyecto
2. Correr el script BORRA_DATOS
3. Ejecutar el programa java CIt3_C-09_m.devia_f.velasquez_creadorDeDatos
4. Seleccionar todas las opciones para generar todos los archivos .csv
5.Importe los archivos .csv a la base de datos usando SQL Developer

En eclipse

1.Insertar la información de su conexión a la base de datos en conexion.properties.
2.En todos los DAO cambiar la constante USUARIO por su usuario de la base de datos.
3. Llevar a cabo pasos 2 y 3 de "Pasos para instalar la aplicación" sobre su base de datos.

Si se desea implementar la base de datos en otro usuario diferente al propuest seguir los siguientes pasos:


---------Pruebas Postman-----------------

6. Abrir la collecciones que se encuentran en It3_C-09_m.devia_f.velasquez\docs\Collections\ en postman
7. Configurar Postman para que exista un delay de 100ms entre cada prueba
8. Ejecutar cada colleccion en el orden que estan, para revisar la documentacion de dichas pruebas ir a It2_C-09_m.devia_f.velasquez\docs\Documentacion Pruebas
