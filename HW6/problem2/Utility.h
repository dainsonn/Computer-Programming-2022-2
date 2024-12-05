#ifndef PROBLEM2_UTILITY_H
#define PROBLEM2_UTILITY_H

#include <ctime>
#include <string>
#include <vector>

std::vector<std::string> split(std::string str, const std::string& delim);

void ltrim(std::string& s);

void rtrim(std::string& s);

void trim(std::string& s);

std::string toString(std::tm rawTime);

std::tm toTime(const std::string& timeStr);

bool operator<(const std::tm& l, const std::tm& r);

#endif // PROBLEM2_UTILITY_H
