# ecommerce-franco-raspo
Proyecto final de curso CoderHouse Java

El proyecto utiliza un config server para obtener los ambientes de UAT y producción
Los archivos de BootStrap se alojan en GitHub.


El proyecto de config server está en GitHub link https://github.com/FrancoRaspo/ecommerce-franco-raspo-config-server
Este debe ejecutarse antes de la aplicación para poder obtener si es necesarios los datos de los ambientes que no son "Dev".

Comando: git clone https://github.com/FrancoRaspo/ecommerce-franco-raspo-config-server.git

Se necesita un repositorio MongoDB.

#### Repositorio - MongoDB - Docker
docker run -d -p 27018:27017 --name ecommerce mongo:latest

Hay 3 ambientes para hacer las pruebas: Dev, UAT y Prod.
Los archivos de "environment" para Postman:

### Test e-commerce Dev.postman_environment.json
Datos de ambiente de Desarrollo para Postman

### Test e-commerce Prod.postman_environment.json
Datos de ambiente de Producción para Postman

### Test e-commerce UAT.postman_environment.json
Datos de ambiente de Testing para Postman

## Archivos - Test - Postman
Carpeta: /ecommerce-franco-raspo/src/main/resources/postman/
Configuración de Test de Postman

En la carpeta Testing-ecommerce, se pueden correr todos los test juntos con "Run Folder".


### CoderHouse.postman_collection.json
* Caso 0 - Inicio aplicación - primer usuario administrador: 
  En la primera ejecución se puede crear un usuario administrador sin necesidad de iniciar sesión,
  luego al existir algún usuario solo usuarios con el rol de administrador pueden crear usuarios con ese rol.
* Caso 1 - El administrador crea nuevos 4 productos: 
  Solo los usuarios administradores pueden crear productos
* Caso 2 - El administrador hace arreglos de los productos cargados: 
  Solo los usuarios administradores pueden administrar productos
* Caso 3 - Se registra un nuevo usuario, se loguea y arma un carrito con 2 productos: 
  Un usuario normal puede elegir productos y armar un carrito
* Caso 4 - El usuario elimina un producto y arma la orden de compra: 
  Un usuario normal puede administrar su carrito y armar una orden de compra
* Caso 5 - El usuario cierra la orden: 
  Un usuario normal puede cerrar o comprar los productos de la orden
* Caso 6 - El usuario crea un carrito y agrega un producto y elimina el carrito: 
  Un usuario normal puede administrar su carrito y productos
* Caso 7 - Usuario no puede administrar productos: 
  Un usuario normal que no es administrador no puede administrar produtos
* Caso 8 - Usuario no puede administrar usuarios: 
  Un uusario normal no puede administrar usuarios
* Caso 9 - Usuario puede modificar sus datos: 
  Un usuario normal solamnete puede modificar sus datos
* Caso 10 - Usuario cambia la clave y se loguea con su nueva clave: 
  Un usuario puede camibar su clave y usarla en un nuevo loguin

