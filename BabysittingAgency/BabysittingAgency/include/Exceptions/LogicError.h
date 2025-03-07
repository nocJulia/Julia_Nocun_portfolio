#ifndef BAYBYSITTINGAGENCY_LOGICERROR_H
#define BAYBYSITTINGAGENCY_LOGICERROR_H

#include <string>
#include <stdexcept>

class LogicError : public std::logic_error {
public:
    explicit LogicError(const std::string& what_arg) : std::logic_error(what_arg) {}
};

#endif //BAYBYSITTINGAGENCY_LOGICERROR_H
