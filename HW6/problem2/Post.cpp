#include "Post.h"

#include <cassert>
#include <sstream>

#include "Utility.h"

Post::Post(std::string user_id, int post_id, std::tm rawTime,
           std::string title, std::string advertising, int likeNumber, std::vector<std::string> content)
        : userId(std::move(user_id)), postId(post_id), rawTime(rawTime),
          title(std::move(title)), advertising(advertising), likeNumber(likeNumber), content(std::move(content)) {
    assert(advertising == "yes" || advertising == "no");
}

std::string Post::str() const {
    std::stringstream ss;
    ss << "id : " << postId << std::endl;
    ss << "created at : " << toString(rawTime) << std::endl;
    ss << "title : " << title << std::endl;
    ss << "content :" << std::endl;
    for (const auto& line: content) {
        ss << line << std::endl;
    }
    return ss.str();
}

std::string Post::summary() const {
    std::stringstream ss;
    ss << "id : " << postId << ", "
       << "liked : " << likeNumber << ", "
       << "created at : " << toString(rawTime) << ", "
       << "title : " << title;
    return ss.str();
}
