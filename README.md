# Medidor-estadistico-metajuego-Magic-The-Gathering
Aplicación de análisis de estadísiticas y ayuda a los jugadores de Magic the Gathering

La aplicación permite que los usuarios introduzcan barajas a la base de datos (únicamente guardando el nombre de las mismas) y posteriormente los permite
registrar sus torneos jugados, introduciendo qué baraja han jugado en el torneos y contra qué baraja y qué resultado han obtenido en cada una de las rondas.

Después el sistema procesa cada uno de los emparejamientos para, por un lado, mostrar al usuario sus estadísticas generales, por otro lado sus estadísticas
desgloasadas por las barajas que ha utilizado y, finalmente, construye un cuadro donde se pueden ver los ratios de victoria generales de todas las barajas, así
como el ratio de victoria de cada baraja frente a cada otra.

# COMPONENTES RELEVANTES DEL REPOSITORIO:
-Memoria y anexos: contiene los documentos correspondientes a la memoria y los anexos del proyecto.
-dist/javadoc: contiene la documentación en javadoc
-ficheros: contiene el fichero de la aplicación desplegado (medidorEstadistico.war),
           el fichero con los datos para la conectividad con la base de datos (config.properties)
           el fichero que requiere ser previamente ejecutado para la creacion de las tablas (script tablas.sql y script tablas.txt).
-links: contiene el enlace a la carpeta compartida de OneDrive donde se alojan los vídeos de exposición, demo de la aplicación y la máquina virtual con todo el entorno instalado para realizar ejecuciones y pruebas.

# INSTALACIÓN LOCAL:
Se aporta un enlace en el documento links.txt con la máquina virtual que dispone de todas las herramientas necesarias.
Si se desea instalar la aplicación localmente, hacen falta las siguientes herramientas:
-Apache tomcat (V8.5 mínimo)
-MySQL Server
-Fichero medidorEstadistico.war (aportado en la raíz del repositorio)

# EJECUCIÓN LOCAL:
Al instalar Apache Tomcat, solicita los puertos de funcionamiento:
  -Dejar el puerto de escucha en 8080
  -Modificar el puerto de cierre en 8085, ya que si se deja en -1 el servicio no se desconecta.
Al instalar MySQL Server, durante la instalación solicita unas credenciales (usuario + contraseña).
Estas credenciales deben modificarse en el fichero config.properties, que se encuentra en la raíz del repositorio 
 (únicamente se deben modificar las filas segunda y tercera (user y password).
Se debe ejecutar el fichro "script tablas.sql", o bien, desde mysqlWorkbench (interfaz gráfica para MySQLServer), copiar el contenido del fichero
 "script tablas.txt", para que se creen las tablas que lee, escribe y manipula la aplicación
El fichero config.properties se debe copiar dentro de la ruta: C:\JAVA
Finalmente, el fichero medidorEstadistico.war se debe copiar en la ruta dónde se haya instalado apache, en la carpeta \webApplications.

Una vez que todo este proceso se haya llevado a cabo:
  -Abrir una consola de comandos COMO ADMINISTRADOR
  -Ejecutar el comando: catalina start
  -Esperar a que el terminar nuevo que se abre indique "Server startup in xxxx ms".
  -Abrir una ventana del navegador web y escribir la url: "localhost:8080/medidorEstadistico"
  -PARA CERRAR LA APLICACIÓN:
    -Después de cerrar la pestaña del navegador, volver a la consola de comandos abierta previamente como administrador y teclear "catalina stop".
