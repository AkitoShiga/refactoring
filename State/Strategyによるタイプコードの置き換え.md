# State/Strategyによるタイプコードの置き換え

## 例
```java
class Employee {
    private int _type;
    static final int ENGINEER = 0;
    static final int SALESMAN = 1;
    static final int MANAGER  = 2;

    Employee(int type) {
        _type = type;
    }

    int payAmount() {
        switch(_type) {
            case ENGINEER:
                return _monthlySalary;
            case SALESMAN:
                return _monthlySalary + _commission;
            case MANAGER:
                return _monthlySalary + _bonus;
            default:
                throw new RuntimeException("不正な従業員");
        }
    }
}
```
### 2 自己カプセル化
```java
Employee(int type) {
    setType(type);
}
int getType() {
    return _type;
}
void setType(int arg) {
    _type = arg;
}

int payAmount() {
    switch(getType()) {
        case ENGINEER:
            return _monthlySalary;
        case SALESMAN:
            return _monthlySalary + _commission;
        case MANAGER:
            return _monthlySalary + _bonus;
        default:
            throw new RuntimeException("不正な従業員");
    }
}
```

### 3 抽象クラスを定義
* タイプコードを返す
```java
abstract class EmployeeType {
    abstract int getTypeCode();
}
class Engineer extends EmployeeType {
    int getTypeCode() {
        return Employee.ENGINEER;
    }
}
class Salesman extends EmployeeType {
    int getTypeCode() {
        return Employee.SALESMAN;
    }
}
class Manager extends EmployeeType {
    int getTypeCode() {
        return Employee.MANAGER;
    }
}
```
### 4 これらのクラスを使用する
* タイプコードを返す
```java
class Employee {
    private EmployeeType _type;
    int getType(){
        return _type.getTypeCode();
    }

    int payAmount() {
        switch(getType()) {
            case ENGINEER:
                return _monthlySalary;
            case SALESMAN:
                return _monthlySalary + _commission;
            case MANAGER:
                return _monthlySalary + _bonus;
            default:
                throw new RuntimeException("不正な従業員");
        }
    }
}
```
### 5 タイプコードを新しいクラスに移動する
```java
class Employee {
    void setType(int arg) {
        _type = EmployeeType.newType(arg);
    }
}
class EmployeeType {
    static EmployeeType newType(int code) {
        switch(code) {
            case ENGINEER:
                return new Engineer();
            case SALESMAN:
                return new Salesman();
            case MANAGER:
                return  new Manager();
            default:
                throw new RuntimeException("不正な従業員");
        }
    }
static final int ENGINEER = 0;
static final int SALESMAN = 1;
static final int MANAGER  = 2;
}
```
### 6 タイプコードの定義を削除して新しいクラスに置き換え
```java
class Employee {
    int payAmount() {
        switch(getType()) {
            case EmployeeType.ENGINEER:
                return new Engineer();
            case EmployeeType.SALESMAN:
                return new Salesman();
            case EmployeeType.MANAGER:
                return  new Manager();
            default:
                throw new RuntimeException("不正な従業員");
        }
    }
}
```
