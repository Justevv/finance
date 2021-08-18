function getIndex(list, id) {
    for (var i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}


var placeApi = Vue.resource('/place{/id}');

Vue.component('place-form', {
    props: ['places', 'placeAttr'],
    data: function() {
        return {
            name: '',
            address: '',
            id: ''
        }
    },
    watch: {
        placeAttr: function(newVal, oldVal) {
            this.name = newVal.name;
            this.address = newVal.address;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
        '<input type="name" placeholder="Write name" v-model="name" />' +
        '<input type="address" placeholder="Write address" v-model="address" />' +
        '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function() {
            let place = new FormData;
            place.append('name', this.name);
            place.append('address', this.address);

            if (this.id) {
                placeApi.update({id: this.id}, place).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.places, data.id);
                        this.places.splice(index, 1, data);
                        this.name = ''
                        this.id = ''
                        this.address = ''
                    })
                )
            } else {
                placeApi.save({}, place).then(result =>
                    result.json().then(data => {
                        this.places.push(data);
                        this.name = ''
                        this.address = ''
                    })
                )
            }
        }
    }
});

Vue.component('place-row', {
    props: ['place', 'editMethod', 'places'],
    template:
        '<tr>' +
        '<td>{{ place.id }}</td>' +
        '<td><span v-if="place.name!==null">{{ place.name }}</span></td>' +
        '<td><span v-if="place.address!==null">{{ place.address }}</span></td>' +
        '<td><input type="button" value="Edit" @click="edit" /></td>' +
        '<td><input type="button" value="X" @click="del" /></td>' +
        '</tr>',
    methods: {
        edit: function() {
            this.editMethod(this.place);
        },
        del: function() {
            placeApi.remove({id: this.place.id}).then(result => {
                if (result.ok) {
                    this.places.splice(this.places.indexOf(this.place), 1)
                }
            })
        }
    }
});

Vue.component('places-list', {
    props: ['places'],
    data: function() {
        return {
            place: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<place-form :places="places" :placeAttr="place" />' +
        '  <table border="1">' +
        '   <caption>Places</caption>'+
        '   <tr>' +
        '    <th>id</th>' +
        '    <th>name</th>' +
        '    <th>address</th>' +
        '    <th>edit</th>' +
        '    <th>delete</th>' +
        '   </tr>'+
        '<place-row v-for="place in places" :key="place.id" :place="place" ' +
        ':editMethod="editMethod" :places="places" />' +
        '</table>' +
        '</div>',
    created: function() {
        placeApi.get().then(result =>
            result.json().then(data =>
                data.forEach(place => this.places.push(place))
            )
        )
    },
    methods: {
        editMethod: function(place) {
            this.place = place;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<places-list :places="places" />',
    data: {
        places: []
    }
});