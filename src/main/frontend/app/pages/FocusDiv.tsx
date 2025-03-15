import {useEffect, useState} from "react";

class Test {
    key: number;
    innerTests: InnerTest[];


    constructor(key: number, innerTests: InnerTest[]) {
        this.key = key;
        this.innerTests = innerTests;
    }
}

class InnerTest {
    key: number;
    name: string;


    constructor(key: number, name: string) {
        this.key = key;
        this.name = name;
    }
}

export default function FocusDiv() {
    const [test, setTest] = useState(new Test(1, [new InnerTest(11, "11")]));
    const [init, setInit] = useState(false);

    useEffect(() => {
        console.log("Test state has changed");
    }, [test]);

    const copyState = () => {
        return new Test(
            test.key,
            test.innerTests
        );
    }

    const changeState = () => {
        const copy = copyState();
        //copy.innerTests.push(new InnerTest(Math.random(), "new-state"));
        console.log("Setting the state");
        setTest(copy);
    }

    const changeInnerState = () => {
        const innerState = test.innerTests.find(innerTest => innerTest.key === 11);
        if (!innerState) {
            throw new Error("InnerState is undefined");
        }
        innerState.name = "changed " + Math.random();
        changeState();
    }

    return (<div>
        <button onClick={changeInnerState}>Change the state</button>
        <div>{test.innerTests.at(0)?.name}</div>
    </div>);
}
