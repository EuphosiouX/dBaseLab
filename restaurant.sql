-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 11, 2022 at 03:42 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `restaurant`
--

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE `invoice` (
  `invoice_id` int(11) NOT NULL,
  `transaction_date` date NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `price` float NOT NULL,
  `staff_id` int(11) NOT NULL,
  `membership_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `invoice`
--

INSERT INTO `invoice` (`invoice_id`, `transaction_date`, `payment_method`, `price`, `staff_id`, `membership_id`) VALUES
(1, '2022-01-11', 'Debit Card', 9.7, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `memberships`
--

CREATE TABLE `memberships` (
  `membership_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone_no` varchar(100) NOT NULL,
  `birth_date` date NOT NULL,
  `join_date` date NOT NULL,
  `points` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `memberships`
--

INSERT INTO `memberships` (`membership_id`, `first_name`, `last_name`, `phone_no`, `birth_date`, `join_date`, `points`) VALUES
(1, 'Scott', 'Cawthon', '2134567', '1987-01-23', '2022-01-11', 0);

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `staff_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `address` varchar(150) NOT NULL,
  `phone_no` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL DEFAULT 'Full Time',
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `staff_category_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`staff_id`, `first_name`, `last_name`, `address`, `phone_no`, `status`, `username`, `password`, `staff_category_id`) VALUES
(1, 'Michael', 'Christopher', 'Jln. Mundu 4 No 8', '085771736879', 'Full Time', 'EuphosX', 'Mc123456', 1),
(2, 'Dennis', 'Nathanael', 'MKG yes', '081234567398', 'Full Time', 'DennisN2', 'DN081234567398', 2),
(3, 'William', 'Afton', 'Fazbear Pizza', '2392367', 'Full Time', 'WilliamA3', 'WA2392367', 5);

-- --------------------------------------------------------

--
-- Table structure for table `staffcategory`
--

CREATE TABLE `staffcategory` (
  `staff_category_id` int(11) NOT NULL,
  `category_name` varchar(50) NOT NULL,
  `hourly_salary` float NOT NULL,
  `working_days` varchar(50) NOT NULL,
  `working_hours` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `staffcategory`
--

INSERT INTO `staffcategory` (`staff_category_id`, `category_name`, `hourly_salary`, `working_days`, `working_hours`) VALUES
(1, 'Manager', 50.6, 'Everyday', 'Afternoon Shift'),
(2, 'Cashier', 20, 'Weekday', 'Afternoon Shift'),
(3, 'Chef', 22.9, 'Everyday', 'Afternoon Shift'),
(4, 'Waiter', 14.7, 'Weekday', 'Night Shift'),
(5, 'Waiter', 14.8, 'Weekend', 'Night Shift');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`invoice_id`),
  ADD KEY `membership_id` (`membership_id`),
  ADD KEY `staff_id` (`staff_id`);

--
-- Indexes for table `memberships`
--
ALTER TABLE `memberships`
  ADD PRIMARY KEY (`membership_id`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`staff_id`),
  ADD KEY `staff_category_id` (`staff_category_id`);

--
-- Indexes for table `staffcategory`
--
ALTER TABLE `staffcategory`
  ADD PRIMARY KEY (`staff_category_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `invoice_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `memberships`
--
ALTER TABLE `memberships`
  MODIFY `membership_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `staff`
--
ALTER TABLE `staff`
  MODIFY `staff_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `staffcategory`
--
ALTER TABLE `staffcategory`
  MODIFY `staff_category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `membership_id` FOREIGN KEY (`membership_id`) REFERENCES `memberships` (`membership_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `staff_id` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON UPDATE CASCADE;

--
-- Constraints for table `staff`
--
ALTER TABLE `staff`
  ADD CONSTRAINT `staff_category_id` FOREIGN KEY (`staff_category_id`) REFERENCES `staffcategory` (`staff_category_id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
