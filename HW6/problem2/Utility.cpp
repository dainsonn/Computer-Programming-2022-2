#include "Utility.h"

#include <algorithm>
#include <iomanip>
#include <sstream>

// https://stackoverflow.com/questions/216823/how-to-trim-an-stdstring
void ltrim(std::string& s) {
    s.erase(s.begin(), std::find_if(s.begin(), s.end(), [](unsigned char ch) {
        return !std::isspace(ch);
    }));
}

void rtrim(std::string& s) {
    s.erase(std::find_if(s.rbegin(), s.rend(), [](unsigned char ch) {
        return !std::isspace(ch);
    }).base(), s.end());
}

void trim(std::string& s) {
    rtrim(s);
    ltrim(s);
}

std::vector<std::string> split(std::string str, const std::string& delim) {
    trim(str);
    size_t pos;
    std::vector<std::string> tokens;
    while ((pos = str.find(delim)) != std::string::npos) {
        std::string token = str.substr(0, pos);
        if (!token.empty()) {
            tokens.push_back(token);
        }
        str.erase(0, pos + 1);
    }
    if (!str.empty()){
        tokens.push_back(str);
    }
    return tokens;
}

std::string toString(std::tm rawTime) {
    std::ostringstream oss;
    oss << std::put_time(&rawTime, "%Y/%m/%d %H:%M:%S");
    return oss.str();
}

std::tm toTime(const std::string& timeStr) {
    std::tm tm;
    std::istringstream ss(timeStr);
    ss >> std::get_time(&tm, "%Y/%m/%d %H:%M:%S");
    return tm;
}

bool operator<(const std::tm& l, const std::tm& r) {
    if (l.tm_year < r.tm_year) {
        return true;
    } else if (l.tm_year > r.tm_year) {
        return false;
    }
    if (l.tm_mon < r.tm_mon) {
        return true;
    } else if (l.tm_mon > r.tm_mon) {
        return false;
    }
    if (l.tm_mday < r.tm_mday) {
        return true;
    } else if (l.tm_mday > r.tm_mday) {
        return false;
    }
    if (l.tm_hour < r.tm_hour) {
        return true;
    } else if (l.tm_hour > r.tm_hour) {
        return false;
    }
    if (l.tm_min < r.tm_min) {
        return true;
    } else if (l.tm_min > r.tm_min) {
        return false;
    }
    if (l.tm_sec < r.tm_sec) {
        return true;
    } else {
        return false;
    }
}
