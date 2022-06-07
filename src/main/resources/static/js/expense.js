function getIndex(list, id) {
    for (let i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

let expenseApi = Vue.resource('/expense{/id}');
let placeApi = Vue.resource('/place{/id}');
let categoryApi = Vue.resource('/category{/id}');

Vue.component('expense-form', {
    props: ['expenses', 'expenseAttr', 'places', 'categories'],
    data: function () {
        return {
            sum: '',
            description: '',
            place: '',
            category: '',
            id: ''
        }
    },
    watch: {
        expenseAttr: function (newVal, oldVal) {
            this.id = newVal.id;
            this.sum = newVal.sum;
            this.description = newVal.description;
            this.place = newVal.place.id;
            this.category = newVal.category.id;
        }
    },
    template:
        '<div>' +
        '<input type="sum" placeholder="Write sum" v-model="sum" />' +
        '<input type="description" placeholder="Write description" v-model="description" />' +
        '<br/>' +
        '<select v-model="category">' +
        // '<option v-for="category in categories" v-bind:value="category.id">{{category.name}}</option>' +
        '<option v-for="category in categories" v-bind:value="category.id" :selected="category == category">{{category.name}}</option>' +
        '</select>' +
        '<br/>' +
        '<select v-model="place">' +
        // '<option v-for="place in places" v-bind:value="place.id">{{place.name}}</option>' +
        '<option v-for="place in places" v-bind:value="place.id" :selected="place == place">{{place.name}}</option>' +
        '</select>' +
        '<br/>' +
        '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function () {
            let expense = new FormData;
            if (this.place !== null && this.place !== undefined && this.place.id != null) {
                expense.append('place', this.place.id);
            } else if (this.place !== null && this.place !== undefined) {
                expense.append('place', this.place);
            }
            if (this.category !== null && this.category !== undefined && this.category.id != null) {
                expense.append('category', this.category.id);
            } else if (this.category !== null && this.category !== undefined) {
                expense.append('category', this.category);
            }
            expense.append('sum', this.sum);
            expense.append('description', this.description);
            // expense.append('place', this.place);
            // expense.append('category', this.category);

            if (this.id) {
                expenseApi.update({id: this.id}, expense).then(result =>
                    result.json().then(data => {
                        // let index = s;
                        let index = getIndex(this.expenses, data.id);
                        this.expenses.splice(index, 1, data);
                        this.sum = ''
                        this.description = ''
                        this.place = ''
                        this.category = ''
                        this.id = ''
                    })
                )
            } else {
                expenseApi.save({}, expense).then(result =>
                    result.json().then(data => {
                        this.expenses.push(data);
                        this.sum = ''
                        this.description = ''
                        this.place = ''
                        this.category = ''
                    })
                )
            }
        }
    }
});

Vue.component('expense-row', {
    props: ['expense', 'editMethod', 'expenses'],
    template:
        '<tr>' +
        '<td>{{ expense.id }}</td>' +
        '<td><span v-if="expense.description!==null">{{ expense.description }}</span></td>' +
        '<td><span v-if="expense.date!==null">{{ expense.date }}</span></td>' +
        '<td><span v-if="expense.sum!==null">{{ expense.sum }}</span></td>' +
        '<td><span v-if="expense.place!==null">{{ expense.place.name }}</span></td>' +
        '<td><span v-if="expense.category!==null">{{ expense.category.name }}</span></td>' +
        '<td><input type="button" value="Edit" @click="edit" /></td>' +
        '<td><input type="button" value="X" @click="del" /></td>' +
        '</tr>',
    methods: {
        edit: function () {
            this.editMethod(this.expense);
        },
        del: function () {
            expenseApi.remove({id: this.expense.id}).then(result => {
                if (result.ok) {
                    this.expenses.splice(this.expenses.indexOf(this.expense), 1)
                }
            })
        }
    }
});

Vue.component('expenses-list', {
    props: ['expenses', 'places', 'categories'],
    data: function () {
        return {
            expense: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<expense-form :expenses="expenses" :places="places" :categories="categories" :expenseAttr="expense" />' +
        '  <table border="1">' +
        '   <caption>Expenses</caption>'+
        '   <tr>' +
        '    <th>id</th>' +
        '    <th>description</th>' +
        '    <th>date</th>' +
        '    <th>sum</th>' +
        '    <th>place</th>' +
        '    <th>category</th>' +
        '    <th>edit</th>' +
        '    <th>delete</th>' +
        '   </tr>'+
        '<expense-row v-for="expense in expenses" :key="expense.id" :expense="expense" ' +
        ':editMethod="editMethod" :expenses="expenses" />' +
        '</table>' +
        '</div>',
    created: function () {
        expenseApi.get().then(result =>
            result.json().then(data =>
                data.forEach(expense => this.expenses.push(expense))
            )
        ),
            placeApi.get().then(result =>
                result.json().then(data =>
                    data.forEach(place => this.places.push(place))
                )
            ),
            categoryApi.get().then(result =>
                result.json().then(data =>
                    data.forEach(category => this.categories.push(category))
                )
            )

    },
    methods: {
        editMethod: function (expense) {
            this.expense = expense;
        }
    }
});

let app = new Vue({
    el: '#app',
    template: '<expenses-list :expenses="expenses" :places="places"  :categories="categories" />',
    data: {
        expenses: [],
        places: [],
        categories: []
    }
});


