function getIndex(list, id) {
    for (let i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

let categoryApi = Vue.resource('/category{/id}');

Vue.component('category-form', {
    props: ['categories', 'categoryAttr'],
    data: function () {
        return {
            name: '',
            parentCategory: '',
            id: ''
        }
    },
    watch: {
        categoryAttr: function (newVal, oldVal) {
            this.name = newVal.name;
            this.parentCategory= newVal.parentCategory.id;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
        '<input type="name" placeholder="Write name" v-model="name" />' +
        '<br/>' +
        '<select v-model="parentCategory">' +
        '<option v-for="category in categories" v-bind:value="category.id" :selected="parentCategory == parentCategory">{{category.name}}</option>' +
        '</select>' +
        '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function () {
            let category = new FormData;
            category.append('name', this.name);
            if (this.parentCategory !== null && this.parentCategory !== undefined && this.parentCategory.id != null) {
                category.append('parentCategory', this.parentCategory.id);
            } else if (this.parentCategory !== null && this.parentCategory !== undefined) {
                category.append('parentCategory', this.parentCategory);
            }

            if (this.id) {
                categoryApi.update({id: this.id}, category).then(result =>
                    result.json().then(data => {
                        let index = getIndex(this.categories, data.id);
                        this.categories.splice(index, 1, data);
                        this.name = ''
                        this.parentCategory = ''
                        this.id = ''
                    })
                    )
            } else {
                categoryApi.save({}, category).then(result =>
                    result.json().then(data => {
                        this.categories.push(data);
                        this.name = ''
                        this.parentCategory = ''
                    })
                    )
            }
        }
    }
});

Vue.component('category-row', {
    props: ['category', 'editMethod', 'categories'],
    template:
        '<tr>' +
        '<td>{{ category.id }}</td>' +
        '<td><span v-if="category.name!==null">{{ category.name }}</span></td>' +
        '<td><span v-if="category.parentCategory!==null">{{ category.parentCategory.name }}</span></td>' +
        // '<span style="position: absolute; right: 0">' +
        '<td><input type="button" value="Edit" @click="edit" /></td>' +
        '<td><input type="button" value="X" @click="del" /></td>' +
        '</tr>',
    methods: {
        edit: function () {
            this.editMethod(this.category);
        },
        del: function () {
            categoryApi.remove({id: this.category.id}).then(result => {
                if (result.ok) {
                    this.categories.splice(this.categories.indexOf(this.category), 1)
                }
            })
        }
    }
});

Vue.component('categories-list', {
    props: ['categories'],
    data: function () {
        return {
            category: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<category-form :categories="categories" :categoryAttr="category"/>' +
        '  <table border="1">' +
        '   <caption>Categories</caption>' +
        '   <tr>' +
        '    <th>id</th>' +
        '    <th>name</th>' +
        '    <th>parentCategory</th>' +
        '    <th>edit</th>' +
        '    <th>delete</th>' +
        '   </tr>' +
        '<category-row v-for="category in categories" :key="category.id" :category="category" ' +
        ':editMethod="editMethod" :categories="categories" />' +
        '</table>' +
        '</div>',
    created: function () {
        categoryApi.get().then(result =>
            result.json().then(data =>
                data.forEach(category => this.categories.push(category))
                )
            )
    },
    methods: {
        editMethod: function (category) {
            this.category = category;
            if (this.category !== null && this.category.parentCategory !== null) {
                this.parentCategory = this.category.parentCategory.id;
            }
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<categories-list :categories="categories"/>',
    data: {
        categories: []
    }
});