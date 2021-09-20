# FactoryMethodによるコンストラクタの置き換え
* オブジェクト生成以上のことをしたい時は、ファクトリメソッドでコンストラクタを置き換えちゃう
* コンストラクタをファクトリメソッドでラップする
* インスタンスの生成部分をファクトリメソッドで置き換える
```java
Employee (int type) {
    _type = type;
}

=>

static Employee create(int type) {
    return new Employee(type);
}
```

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

}
```
* Employeeのサブクラスを作りたくなった

### 1 サブクラス作成
```java
static Employee create(int type) {
    return new Employee(type);
}
```

### 2 インスタンスの呼び出し先をファクトリメソッドに置き換え
```java
// クライアント
Employee eng = Employee.create(Employee.ENGINEER);

class Employee {
    private Employee(int type) {
        _type = type;
    }
    static Employee create(int type) {
        return new Employee(type);
    }
}
```

### 3 Stringでサブクラスを作成
```java
static Employee create(int type) {
    switch(type) {
        case ENGINEER:
            return new Engineer();
        case SALESMAN:
            return new Salesman();
        case MANAGER :
            return new Manager();
        default:
            throw new IllegalArgumentException("不正なタイプコード");
    }
}
```
### 4 Class.forNameを使う
* いちいちインスタンス化しなくてすむ
* クライアントからサブクラスを隠蔽できる
```java
static Employee {
    static Employee create(String name) {
        try {
            return (Employee) Class.forName(name).newInstance();
        } catch(exception e) {
            throw new IllegalArgumentException("インスタンス化不能" + name);
        }
    }
}
```

### 5 こっちを使う
```java
static Employee create(int type) {
    switch(type) {
        case ENGINEER:
            return  new create("Engineer");
        case SALESMAN:
            return  new create("Salesman");
        case MANAGER:
            return  new create("Manager");
        default:
            throw new IllegalArgumentException("不正なタイプコード");
    }
}
```

### 6 クライアントも変更
```java
Employee.create("Engineer");
```
