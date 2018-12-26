-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 26, 2018 at 03:07 AM
-- Server version: 10.1.24-MariaDB
-- PHP Version: 7.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `timetracker`
--
CREATE DATABASE IF NOT EXISTS `timetracker` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `timetracker`;

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `emp_id` int(10) NOT NULL,
  `emp_fname` varchar(75) NOT NULL,
  `emp_mname` varchar(50) NOT NULL,
  `emp_lname` varchar(50) NOT NULL,
  `emp_age` int(11) NOT NULL,
  `emp_gender` varchar(6) NOT NULL,
  `emp_address` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`emp_id`, `emp_fname`, `emp_mname`, `emp_lname`, `emp_age`, `emp_gender`, `emp_address`) VALUES
(0, 'Administrator', 'Administrator', 'Administrator', 50, 'Male', 'General Maxilom Avenue, Cebu City'),
(1, 'Juneil', 'Deiparine', 'Gamallo', 34, 'Male', '180 Cabantan Street, Barangay Luz, Cebu City'),
(2, 'Nympha', 'Gamallo', 'Panganiban', 32, 'Female', '180 Cabantan Street, Barangay Luz, Cebu City'),
(3, 'Susana', 'Vestil', 'Deiparine', 56, 'Female', '180 Cabantan Street, Barangay Luz, Cebu City');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD UNIQUE KEY `emp_id` (`emp_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
