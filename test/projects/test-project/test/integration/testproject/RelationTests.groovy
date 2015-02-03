package testproject

import spock.lang.Specification

class RelationTests extends Specification {

    public void testDefaultDataSource() {
        setup:

            Foo foo = new Foo(foo: "foo1")
            foo.save()

            Bar bar = new Bar(bar: "bar1")
            bar.save()

        when:

            foo.setBar(bar)

        then:

            foo.getBar() == bar
    }

    public void testIsDatasourceAware() {
        setup:

            Bar bar1 = new Bar(bar: "bar1")
            bar1.save()

            Bar bar2 = new Bar(bar: "bar2")
            bar2.lookup.save()

            FooLookup fooLookup = new FooLookup(foo: "fooLookup")
            fooLookup.save()

        when:

            fooLookup.setBar(bar2)

        then:

            bar2.id
            Bar.lookup.get(bar2.id) == bar2
            Bar.get(bar2.id) != bar2
            Bar.lookup.count() == 1
            Bar.count() == 1
            fooLookup.barId == bar2.id
            fooLookup.getBar() == Bar.lookup.get(bar2.id)
            fooLookup.getBar() == bar2
    }
}
