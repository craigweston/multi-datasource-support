package testproject

import spock.lang.Specification
import spock.lang.Shared
import grails.test.mixin.Mock

@Mock([Foo, Bar, Alpha, Beta])
class RelationUnitTests extends Specification {

    void "test getter"() {
        given:
            Bar bar = new Bar(bar: "bar1")
            bar.save()

            Foo foo = new Foo(foo: "foo1")
        when:
            foo.barId = bar.id
        then:
            foo.barId
            Bar.get(bar.id) == foo.getBar()
    }

    void "test setter"() {
        given:
            Bar bar = new Bar(bar: "bar1")
            bar.save()

            Foo foo = new Foo(foo: "foo1")
        when:
            foo.setBar(bar)
        then:
            foo.barId == bar.id
            foo.getBar() == bar
    }

    void "test setter with unsaved arg"() {
        given:
            Bar bar2 = new Bar(bar: "bar2")

            Foo foo = new Foo(foo: "foo1")
        when:
            foo.setBar(bar2)
        then:
            !(foo.barId)
            !(foo.getBar())
    }

    void "test setter with unsaved arg that was previously set"() {
        given:
            Bar bar2 = new Bar(bar: "bar2")

            Bar bar = new Bar(bar: "bar1")
            bar.save()

            Foo foo = new Foo(foo: "foo1")
        when:
            foo.setBar(bar)
            foo.setBar(bar2)
        then:
            foo.barId
            foo.getBar() == bar
    }

    void "test setter with null arg"() {
        given:
            Foo foo = new Foo(foo: "foo1")
        when:
            foo.setBar(null)
        then:
            !(foo.barId)
            !(foo.getBar())
    }

    void "test setter with null arg that was previously set"() {
        given:
            Bar bar = new Bar(bar: "bar1")
            bar.save()

            Foo foo = new Foo(foo: "foo1")
            foo.setBar(bar)
        when:
            foo.setBar(null)
        then:
            foo.barId
            foo.getBar() == bar
    }

    void "test getter return null for non existent id"() {
        given:
            Bar bar = new Bar(bar: "bar1")
            bar.save()
            Foo foo = new Foo(foo: "foo1")
        when:
            foo.barId = bar.id+1
        then:
            !(foo.getBar())
    }

    void "test that transients list created"() {
        expect:
            Foo.transients
            Foo.transients.contains("bar")
    }

    void "test that transients list was added to"() {
        given:
            Alpha alpha = new Alpha(alpha: "alpha")
            alpha.save()

            Beta beta = new Beta(beta: "beta")
            beta.save()
        expect:
            Alpha.transients
            Alpha.transients.contains("beta")
            Alpha.transients.contains("bar")
    }

    void "test id field works with gorm generated id"() {
        given:
            Alpha alpha = new Alpha(alpha: "alpha")
            alpha.save()

            Beta beta = new Beta(beta: "beta")
            beta.save()
        expect:
            Alpha.getField("betaId").type == Long
    }
}
