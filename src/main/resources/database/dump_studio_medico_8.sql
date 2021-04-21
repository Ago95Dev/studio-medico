-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: studio_medico
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contratti`
--

DROP TABLE IF EXISTS `contratti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contratti` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipologia_contratto` enum('Forfettario','Presenze','Prestazioni') NOT NULL,
  `quota` float DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contratti`
--

LOCK TABLES `contratti` WRITE;
/*!40000 ALTER TABLE `contratti` DISABLE KEYS */;
INSERT INTO `contratti` VALUES (1,'Forfettario',0),(2,'Presenze',0),(3,'Prestazioni',0);
/*!40000 ALTER TABLE `contratti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fatture`
--

DROP TABLE IF EXISTS `fatture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fatture` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pagato` tinyint(1) NOT NULL,
  `prenotazioni_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fatture_prenotazioni1_idx` (`prenotazioni_id`),
  CONSTRAINT `fk_fatture_prenotazioni1` FOREIGN KEY (`prenotazioni_id`) REFERENCES `prenotazioni` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fatture`
--

LOCK TABLES `fatture` WRITE;
/*!40000 ALTER TABLE `fatture` DISABLE KEYS */;
INSERT INTO `fatture` VALUES (1,0,1);
/*!40000 ALTER TABLE `fatture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prenotazioni`
--

DROP TABLE IF EXISTS `prenotazioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prenotazioni` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_medico` int NOT NULL,
  `id_paziente` int NOT NULL,
  `inizio` time NOT NULL,
  `fine` time NOT NULL,
  `id_tipo_visita` int NOT NULL,
  `id_turno` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_medico` (`id_medico`),
  KEY `id_paziente` (`id_paziente`),
  KEY `prenotazioni_ibfk_3_idx` (`id_tipo_visita`),
  KEY `prenotazioni_ibfk_4_idx` (`id_turno`),
  CONSTRAINT `prenotazioni_ibfk_1` FOREIGN KEY (`id_medico`) REFERENCES `utenti` (`id`),
  CONSTRAINT `prenotazioni_ibfk_2` FOREIGN KEY (`id_paziente`) REFERENCES `utenti` (`id`),
  CONSTRAINT `prenotazioni_ibfk_3` FOREIGN KEY (`id_tipo_visita`) REFERENCES `tipi_visita` (`specializzazione_di_competenza`),
  CONSTRAINT `prenotazioni_ibfk_4` FOREIGN KEY (`id_turno`) REFERENCES `turni` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazioni`
--

LOCK TABLES `prenotazioni` WRITE;
/*!40000 ALTER TABLE `prenotazioni` DISABLE KEYS */;
INSERT INTO `prenotazioni` VALUES (1,2,1,'09:30:00','10:00:00',1,1),(2,2,1,'12:30:00','13:30:00',4,1),(3,2,1,'09:00:00','09:30:00',1,1),(4,2,1,'15:30:00','16:00:00',1,1),(5,2,1,'10:30:00','12:30:00',6,1),(6,4,1,'10:30:00','12:30:00',6,3);
/*!40000 ALTER TABLE `prenotazioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specializzazioni`
--

DROP TABLE IF EXISTS `specializzazioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specializzazioni` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipologia` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specializzazioni`
--

LOCK TABLES `specializzazioni` WRITE;
/*!40000 ALTER TABLE `specializzazioni` DISABLE KEYS */;
INSERT INTO `specializzazioni` VALUES (1,'Fisioterapia'),(2,'Nutrizionista'),(3,'Cardiologia'),(4,'Senologia'),(5,'Otorinolaringoiatria'),(6,'Ortopedia'),(7,'Urologia'),(8,'Neurologia'),(9,'Gastroenterologia'),(10,'Oncologia'),(11,'Neurochirurgia'),(12,'Medicina Interna'),(13,'Ginecologia'),(14,'Psicologia'),(15,'Chirurgia Vascolare'),(16,'Ostetricia'),(17,'Andrologia'),(18,'Traumatologia');
/*!40000 ALTER TABLE `specializzazioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipi_visita`
--

DROP TABLE IF EXISTS `tipi_visita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipi_visita` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(40) NOT NULL,
  `specializzazione_di_competenza` int NOT NULL DEFAULT '0',
  `durata` time NOT NULL DEFAULT '00:00:00',
  `prezzo` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `specializzazione_di_competenza` (`specializzazione_di_competenza`),
  CONSTRAINT `tipi_visita_ibfk_1` FOREIGN KEY (`specializzazione_di_competenza`) REFERENCES `specializzazioni` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipi_visita`
--

LOCK TABLES `tipi_visita` WRITE;
/*!40000 ALTER TABLE `tipi_visita` DISABLE KEYS */;
INSERT INTO `tipi_visita` VALUES (1,'Holter pressorio',3,'30:00:00',50),(2,'Chinesiterapia',1,'60:00:00',100),(3,'Visita nutrizionistica',2,'60:00:00',130),(4,'Laserterapia',3,'60:00:00',120),(5,'Ecocardiogramma',3,'30:00:00',80),(6,'ECG da sforzo',3,'120:00:00',100),(7,'Linfodrenaggio',1,'60:00:00',35),(8,'Ultrasuonoterapia',1,'60:00:00',110),(9,'Tecarterterapia',1,'30:00:00',50),(10,'Visita senologica',4,'30:00:00',55),(11,'Visita di controllo',2,'30:00:00',70),(12,'Visita neurochirurgica',11,'120:00:00',150),(13,'Visita urologica',7,'30:00:00',90),(14,'Elettromiografia',8,'120:00:00',120),(15,'Breath Test',9,'30:00:00',45),(16,'Riabilitazione posturale',1,'120:00:00',50),(17,'Pap Test',13,'30:00:00',50),(18,'Ecografia ginecologica',13,'30:00:00',60),(19,'Visita oncologica',10,'60:00:00',90),(20,'Visita gastroenterologica',9,'60:00:00',100),(21,'Elettroterapia',1,'30:00:00',80),(22,'Visita otorinolaringoiatrica',5,'30:00:00',50),(23,'Esame audiometrico',5,'30:00:00',35),(24,'Fibrolaringoscopia',5,'60:00:00',90),(25,'Esame destibolare',5,'120:00:00',120),(26,'Visita ortopedica',6,'30:00:00',65),(27,'Visita traumatologica',18,'30:00:00',75),(28,'Visita andrologica',17,'30:00:00',95),(29,'Visita andrologica + Ecografia',17,'60:00:00',110),(30,'Visita urologica + Ecografia',7,'60:00:00',110),(31,'Visita neurologica',8,'120:00:00',150),(32,'Visita proctologica',9,'60:00:00',120),(33,'Idrocolonterapia',9,'120:00:00',160),(34,'Visita endocrinologica',12,'60:00:00',100),(35,'Visita ematologica',12,'60:00:00',110),(36,'Visita ginecologica',13,'60:00:00',100),(37,'Visita ostetrica',16,'30:00:00',55),(38,'Visita + Ecocolordoppler',15,'120:00:00',180),(39,'Visita psicologica',14,'120:00:00',120);
/*!40000 ALTER TABLE `tipi_visita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `turni`
--

DROP TABLE IF EXISTS `turni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `turni` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_medico` int NOT NULL,
  `data` date NOT NULL,
  `accettato` tinyint(1) NOT NULL DEFAULT '0',
  `in_corso` tinyint(1) NOT NULL DEFAULT '0',
  `ora_inizio` time NOT NULL,
  `ora_fine` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_medico` (`id_medico`),
  CONSTRAINT `turni_ibfk_1` FOREIGN KEY (`id_medico`) REFERENCES `utenti` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turni`
--

LOCK TABLES `turni` WRITE;
/*!40000 ALTER TABLE `turni` DISABLE KEYS */;
INSERT INTO `turni` VALUES (1,2,'2021-04-17',1,0,'08:00:00','17:00:00'),(2,2,'2021-04-25',1,0,'09:00:00','13:00:00'),(3,4,'2021-04-22',1,0,'14:00:00','19:00:00'),(4,2,'2021-03-22',1,0,'15:00:00','20:00:00');
/*!40000 ALTER TABLE `turni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_contratto` int DEFAULT NULL,
  `nome` varchar(25) DEFAULT NULL,
  `cognome` varchar(25) DEFAULT NULL,
  `email` varchar(25) NOT NULL,
  `telefono` varchar(25) DEFAULT NULL,
  `codice_fiscale` varchar(25) NOT NULL,
  `data_di_nascita` date DEFAULT NULL,
  `luogo_di_nascita` varchar(25) DEFAULT NULL,
  `password` varchar(40) DEFAULT NULL,
  `id_specializzazione` int DEFAULT NULL,
  `ruolo` enum('paziente','medico','segretaria') NOT NULL,
  `numeropresenze` int DEFAULT NULL,
  `numeroprestazioni` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `id_specializzazione` (`id_specializzazione`),
  KEY `id_contratto` (`id_contratto`),
  CONSTRAINT `utenti_ibfk_1` FOREIGN KEY (`id_specializzazione`) REFERENCES `specializzazioni` (`id`),
  CONSTRAINT `utenti_ibfk_2` FOREIGN KEY (`id_contratto`) REFERENCES `contratti` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (1,NULL,'paziente','paziente','paziente@email.com','123456789','pazienteCF','2000-02-03','Roma','paziente',NULL,'paziente',NULL,NULL),(2,1,'medico','medico','medico@email.com','123','medicoCF','1980-02-02','Roma','medico',3,'medico',NULL,NULL),(3,NULL,'segretaria','segretaria','segretaria@email.com','456','segretariaCF','2000-02-05','Roma','segretaria',NULL,'segretaria',NULL,NULL),(4,2,'medico2','medico2','medico2@email.com','79561543','medico2CF','1994-05-12','L\'Aquila','medico2',3,'medico',NULL,NULL);
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-21 16:38:10
