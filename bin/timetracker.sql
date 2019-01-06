-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 06, 2019 at 09:13 PM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.1.25

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

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `emp_id` varchar(5) NOT NULL,
  `passcode` varchar(15) NOT NULL,
  `emp_fname` varchar(25) NOT NULL,
  `emp_mname` varchar(25) NOT NULL,
  `emp_lname` varchar(25) NOT NULL,
  `emp_age` int(3) NOT NULL,
  `emp_gender` varchar(6) NOT NULL,
  `emp_address` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`emp_id`, `passcode`, `emp_fname`, `emp_mname`, `emp_lname`, `emp_age`, `emp_gender`, `emp_address`) VALUES
('0', '0', 'Administrator', 'Administrator', 'Administrator', 50, 'Male', 'General Maxilom Avenue, Cebu City'),
('1', '1', 'Juneil', 'Deiparine', 'Gamallo', 34, 'Male', '180 Cabantan Street, Barangay Luz, Cebu City'),
('2', '2', 'Nympha', 'Gamallo', 'Panganiban', 32, 'Female', '180 Cabantan Street, Barangay Luz, Cebu City'),
('3', '3', 'Susana', 'Vestil', 'Deiparine', 56, 'Female', '180 Cabantan Street, Barangay Luz, Cebu City');

-- --------------------------------------------------------

--
-- Table structure for table `timelogs`
--

CREATE TABLE `timelogs` (
  `emp_id` varchar(5) NOT NULL,
  `date_in` date NOT NULL,
  `time_in` time(6) NOT NULL,
  `date_out` date NOT NULL,
  `time_out` time(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `timelogs`
--

INSERT INTO `timelogs` (`emp_id`, `date_in`, `time_in`, `date_out`, `time_out`) VALUES
('2', '2018-12-30', '14:18:15.000000', '2018-12-30', '14:24:53.000000'),
('2', '2018-12-30', '14:25:11.000000', '2018-12-30', '20:27:29.000000'),
('2', '2018-12-30', '21:07:19.000000', '0000-00-00', '00:00:00.000000'),
('1', '2019-01-01', '03:47:25.000000', '2019-01-01', '03:48:13.000000');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`emp_id`),
  ADD UNIQUE KEY `emp_id` (`emp_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
