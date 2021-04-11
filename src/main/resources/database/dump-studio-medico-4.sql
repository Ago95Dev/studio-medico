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
  `tipologia_contratto` enum('forfettario','presenze','prestazioni') DEFAULT NULL,
  `quota` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contratti`
--

LOCK TABLES `contratti` WRITE;
/*!40000 ALTER TABLE `contratti` DISABLE KEYS */;
INSERT INTO `contratti` VALUES (1,'forfettario',NULL),(2,'presenze',NULL),(3,'prestazioni',NULL);
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
  `id_prenotazione` int DEFAULT NULL,
  `id_segretaria` int DEFAULT NULL,
  `pagato` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_prenotazione` (`id_prenotazione`),
  KEY `id_segretaria` (`id_segretaria`),
  CONSTRAINT `fatture_ibfk_1` FOREIGN KEY (`id_prenotazione`) REFERENCES `prenotazioni` (`id`),
  CONSTRAINT `fatture_ibfk_2` FOREIGN KEY (`id_segretaria`) REFERENCES `utenti` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fatture`
--

LOCK TABLES `fatture` WRITE;
/*!40000 ALTER TABLE `fatture` DISABLE KEYS */;
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
  `id_medico` int DEFAULT NULL,
  `id_paziente` int DEFAULT NULL,
  `id_visita` int DEFAULT NULL,
  `inizio` datetime DEFAULT NULL,
  `fine` datetime DEFAULT NULL,
  `tipo` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_medico` (`id_medico`),
  KEY `id_paziente` (`id_paziente`),
  CONSTRAINT `prenotazioni_ibfk_1` FOREIGN KEY (`id_medico`) REFERENCES `utenti` (`id`),
  CONSTRAINT `prenotazioni_ibfk_2` FOREIGN KEY (`id_paziente`) REFERENCES `utenti` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazioni`
--

LOCK TABLES `prenotazioni` WRITE;
/*!40000 ALTER TABLE `prenotazioni` DISABLE KEYS */;
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
  `tipologia` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specializzazioni`
--

LOCK TABLES `specializzazioni` WRITE;
/*!40000 ALTER TABLE `specializzazioni` DISABLE KEYS */;
INSERT INTO `specializzazioni` VALUES (1,'Fisioterapia'),(2,'Nutrizionista'),(3,'Cardiologia'),(4,'Senologia'),(5,'Otorinolaringoiatria'),(6,'Ortopedia'),(7,'Urologia'),(8,'Neurologia'),(9,'Gastroenterologia'),(10,'Oncologia'),(11,'Neurochirurgia'),(12,'MedicinaInterna'),(13,'Ginecologia'),(14,'Psicologia'),(15,'ChirurgiaVascolare'),(16,'Ostetricia'),(17,'Andrologia'),(18,'Traumatologia');
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
  `tipo` varchar(25) DEFAULT NULL,
  `specializzazione_di_competenza` int DEFAULT NULL,
  `durata` time NOT NULL,
  `prezzo` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `specializzazione_di_competenza` (`specializzazione_di_competenza`),
  CONSTRAINT `tipi_visita_ibfk_1` FOREIGN KEY (`specializzazione_di_competenza`) REFERENCES `specializzazioni` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipi_visita`
--

LOCK TABLES `tipi_visita` WRITE;
/*!40000 ALTER TABLE `tipi_visita` DISABLE KEYS */;
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
  `id_medico` int DEFAULT NULL,
  `data` date DEFAULT NULL,
  `accettato` tinyint(1) DEFAULT NULL,
  `in_corso` tinyint(1) DEFAULT NULL,
  `ora_inizio` varchar(45) DEFAULT NULL,
  `ora_fine` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_medico` (`id_medico`),
  CONSTRAINT `turni_ibfk_1` FOREIGN KEY (`id_medico`) REFERENCES `utenti` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turni`
--

LOCK TABLES `turni` WRITE;
/*!40000 ALTER TABLE `turni` DISABLE KEYS */;
INSERT INTO `turni` VALUES (1,18,'2021-04-23',0,0,'11:58','14:58');
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
  `email` varchar(25) DEFAULT NULL,
  `telefono` varchar(25) DEFAULT NULL,
  `codice_fiscale` varchar(25) DEFAULT NULL,
  `data_di_nascita` date DEFAULT NULL,
  `luogo_di_nascita` varchar(25) DEFAULT NULL,
  `password` varchar(40) DEFAULT NULL,
  `id_specializzazione` int DEFAULT NULL,
  `ruolo` enum('paziente','medico','segretaria') DEFAULT NULL,
  `numeropresenze` int DEFAULT NULL,
  `numeroprestazioni` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `id_specializzazione` (`id_specializzazione`),
  KEY `id_contratto` (`id_contratto`),
  CONSTRAINT `utenti_ibfk_1` FOREIGN KEY (`id_specializzazione`) REFERENCES `specializzazioni` (`id`),
  CONSTRAINT `utenti_ibfk_2` FOREIGN KEY (`id_contratto`) REFERENCES `contratti` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (1,NULL,'Lorenzo','Iapadre','lorenzoiapadre@mail.com','345366522','lrnzipdr234567maq',NULL,NULL,'lorenzoiapadre',NULL,'paziente',NULL,NULL),(2,NULL,'Tizia','Sempronia','tiziasempronia@mail.com','4561321789','tzsmprn27364hdk39','1959-01-12','Roma','tiziasempronia',NULL,'segretaria',NULL,NULL),(3,1,'Patch','Adams','patchadams@mail.com','346975318454','reofoenf43o534oofn','1945-05-28','Washington','patchadams',3,'medico',9,14),(4,NULL,'franco','falleroni','francofalleroni@mail.com','65132156546851','usysdugcysicbdad','2021-03-09','Porto D\'Ascoli','francofalleroni',NULL,'paziente',NULL,NULL),(5,NULL,'Mario','Rossi','mariorossi@mail.com','12398745673','mrrs123hjdu456maq','1992-03-11','L\'Aquila','mariorossi',NULL,'paziente',NULL,NULL),(18,3,'Lele','Martini','lelemartini@mail.com','4613258976426','alskdjfhgyturieoq','2021-04-15','Roma','lelemartini',9,'medico',0,0);
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

-- Dump completed on 2021-04-11 16:51:56
